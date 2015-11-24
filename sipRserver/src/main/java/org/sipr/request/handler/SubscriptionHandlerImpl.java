package org.sipr.request.handler;

import org.sipr.core.domain.SubscriptionBinding;
import org.sipr.core.service.SubscriptionBindingsService;
import org.sipr.core.sip.request.handler.SubscriptionHandler;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.request.notify.NotifyBodyBuilder;
import org.sipr.utils.SipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sip.*;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
public class SubscriptionHandlerImpl implements SubscriptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionHandlerImpl.class);

    @Inject
    MessageFactory messageFactory;

    @Inject
    HeaderFactory headerFactory;

    @Inject
    SipUtils sipUtils;

    @Inject
    SubscriptionBindingsService subscriptionBindingsService;

    @Value("${sip.registration.expires}")
    private int serverExpire;

    @Inject
    List<NotifyBodyBuilder> notifyBuilders;

    Map<String, NotifyBodyBuilder> notifyBuildersMap;

    @PostConstruct
    void init() {
        notifyBuildersMap = notifyBuilders.stream().collect(toMap(NotifyBodyBuilder::getEventType, (r) -> r));
    }

    @Override
    public void handleRequest(RequestEvent requestEvent) throws RequestException {
        SipProvider sipProvider = (SipProvider) requestEvent.getSource();
        Request request = requestEvent.getRequest();

        EventHeader eventHeader = (EventHeader) request.getHeader(EventHeader.NAME);
        String eventType = eventHeader.getEventType();
        NotifyBodyBuilder notifyBuilder = notifyBuildersMap.get(eventType);
        if (notifyBuilder == null) {
            throw new RequestException(Response.NOT_IMPLEMENTED);
        }

        Response response = null;

        try {
            ServerTransaction st = requestEvent.getServerTransaction();
            if (st == null) {
                st = sipProvider.getNewServerTransaction(request);
            }

            Dialog dialog = requestEvent.getDialog();
            if (dialog == null) {
                String toTag = Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
                response = messageFactory.createResponse(Response.ACCEPTED, request);

                ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
                toHeader.setTag(toTag);

                dialog = st.getDialog();
                dialog.terminateOnBye(false);
            } else {
                response = messageFactory.createResponse(Response.OK, request);
            }

            ContactHeader contactHeader = sipUtils.createProviderContactHeader(sipProvider);
            response.addHeader(contactHeader);

            ExpiresHeader expires = (ExpiresHeader) request.getHeader(ExpiresHeader.NAME);
            if (expires == null) {
                expires = headerFactory.createExpiresHeader(serverExpire);
            }
            response.addHeader(expires);

            boolean unsubscribe = expires.getExpires() == 0;

            String contactUri = sipUtils.getFirstContactUri(request);
            SubscriptionBinding subscription = (SubscriptionBinding) subscriptionBindingsService.findByContactAndType(contactUri, eventType);
            if (subscription == null) {
                subscription = (SubscriptionBinding) subscriptionBindingsService.createSubscription(sipUtils.extractAuthUser(request),
                        contactUri, ((CallIdHeader) request.getHeader(CallIdHeader.NAME)).getCallId(),
                        ((CSeqHeader) request.getHeader(CSeqHeader.NAME)).getSeqNumber(), expires.getExpires(), eventType);
            }

            if (!unsubscribe) {
                subscriptionBindingsService.saveSubscription(subscription);
            }
            st.sendResponse(response);

            SubscriptionStateHeader state = headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.ACTIVE);
            if (unsubscribe) {
                subscriptionBindingsService.deleteSubscription(subscription);
                state = headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.TERMINATED);
                state.setReasonCode("deactivated");
            }

            Request notifyRequest = dialog.createRequest(Request.NOTIFY);
            notifyRequest.setHeader(contactHeader);
            notifyRequest.setHeader(eventHeader);
            notifyRequest.addHeader(state);

            if (!unsubscribe) {
                notifyBuilder.addMessageBody(notifyRequest);
            }

            ClientTransaction ct = sipProvider.getNewClientTransaction(notifyRequest);
            dialog.sendRequest(ct);

        } catch (InvalidArgumentException | ParseException | SipException ex) {
            throw new RequestException(Response.SERVER_INTERNAL_ERROR);
        }
    }
}
