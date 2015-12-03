package org.sipr.request.notify;

import gov.nist.javax.sip.message.Content;
import gov.nist.javax.sip.message.ContentImpl;
import gov.nist.javax.sip.message.MultipartMimeContentImpl;
import org.sipr.core.domain.UserPresence;
import org.sipr.core.service.UserPresenceService;
import org.sipr.request.handler.SubscriptionRequest;
import org.sipr.request.notify.events.DndEvent;
import org.sipr.request.notify.events.ForwardEvent;
import org.sipr.request.notify.events.NotifyEventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.InvalidArgumentException;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class DeviceFeatureKeyContentBuilder implements NotifyContentBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceFeatureKeyContentBuilder.class);
    private static final String SIPR_BOUNDARY = "SipRBoundary";

    @Inject
    HeaderFactory headerFactory;

    @Inject
    UserPresenceService presenceService;

    @Inject
    NotifySender sender;

    @Inject
    NotifyEventFactory eventFactory;

    @Override
    public void addContent(Request notifyRequest, SubscriptionRequest request) throws ParseException, InvalidArgumentException {
        String content = new String(request.getRequest().getRawContent(), StandardCharsets.UTF_8);

        UserPresence presence = presenceService.getPresence(request.getUser());

        if (isEmpty(content)) {
            ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("multipart", "mixed");
            contentTypeHeader.setParameter(MultipartMimeContentImpl.BOUNDARY, SIPR_BOUNDARY);

            notifyRequest.setContent(getInitialMultipartContent(contentTypeHeader, presence).toString(), contentTypeHeader);
        } else {
            String eventResponse = getEventResponse(presence, content);
            presenceService.savePresence(presence);

            ContentTypeHeader eventHeader = getAsFeatureEventHeader();
            notifyRequest.setContent(eventResponse, eventHeader);
            sender.sendNotifyToAllSubscribers(request, eventHeader, eventResponse);
        }
    }

    String getEventResponse(UserPresence presence, String eventContent) {
        Object event = eventFactory.createEvent(eventContent);

        if (event instanceof DndEvent) {
            DndEvent dndEvent = (DndEvent) event;
            return dndEvent.buildEventResponse(presence);
        } else if (event instanceof ForwardEvent) {
            ForwardEvent fwdEvent = (ForwardEvent) event;
            return fwdEvent.buildEventResponse(presence);
        }
        return null;
    }

    MultipartMimeContentImpl getInitialMultipartContent(ContentTypeHeader contentTypeHeader, UserPresence presence) throws ParseException, InvalidArgumentException {
        MultipartMimeContentImpl multipartMimeContent = new MultipartMimeContentImpl(contentTypeHeader);
        multipartMimeContent.addContent(createContent(DndEvent.getDndEvent(presence)));
        multipartMimeContent.addContent(createContent(ForwardEvent.getForwardImmediateEvent(presence)));
        multipartMimeContent.addContent(createContent(ForwardEvent.getForwardOnBusyEvent(presence)));
        multipartMimeContent.addContent(createContent(ForwardEvent.getForwardNoAnswerEvent(presence)));
        return multipartMimeContent;
    }

    Content createContent(String responseContent) throws ParseException, InvalidArgumentException {
        ContentImpl content = new ContentImpl(responseContent);
        content.addExtensionHeader(getAsFeatureEventHeader());
        content.addExtensionHeader(headerFactory.createContentLengthHeader(responseContent.length()));
        return content;
    }

    ContentTypeHeader getAsFeatureEventHeader() throws ParseException {
        return headerFactory.createContentTypeHeader("application", "x-as-feature-event+xml");
    }

    @Override
    public String getEventType() {
        return "as-feature-event";
    }
}
