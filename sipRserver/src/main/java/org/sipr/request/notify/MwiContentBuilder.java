package org.sipr.request.notify;

import gov.nist.javax.sip.message.ContentImpl;
import org.sipr.request.handler.SubscriptionRequest;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import java.text.ParseException;

@Component
public class MwiContentBuilder implements NotifyContentBuilder {

    @Inject
    HeaderFactory headerFactory;

    @Override
    public void addContent(Request notifyRequest, SubscriptionRequest request) throws ParseException {
        ContentImpl content = new ContentImpl("");

        // TODO implement MailboxService, get some real data
        content.addExtensionHeader(headerFactory.createHeader("Messages-Waiting", "no"));
        content.addExtensionHeader(headerFactory.createHeader("Message-Account", request.getUser()));
        content.addExtensionHeader(headerFactory.createHeader("Voice-Message", "8/11 (0/0)"));

        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "simple-message-summary");
        notifyRequest.setContent(content, contentTypeHeader);
    }

    @Override
    public String getEventType() {
        return "message-summary";
    }
}
