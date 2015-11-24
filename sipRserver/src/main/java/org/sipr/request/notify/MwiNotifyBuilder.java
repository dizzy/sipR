package org.sipr.request.notify;

import gov.nist.javax.sip.message.ContentImpl;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import java.text.ParseException;

@Component
public class MwiNotifyBuilder implements NotifyBodyBuilder {

    @Inject
    HeaderFactory headerFactory;

    @Override
    public void addMessageBody(Request notifyRequest) throws ParseException {
        ContentImpl content = new ContentImpl("");
        // TODO implement MailboxService, get some real data
        content.addExtensionHeader(headerFactory.createHeader("Messages-Waiting", "yes"));
        content.addExtensionHeader(headerFactory.createHeader("Message-Account", "200@sipr.org"));
        content.addExtensionHeader(headerFactory.createHeader("Voice-Message", "11/12 (0/0)"));

        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "simple-message-summary");
        notifyRequest.setContent(content, contentTypeHeader);
    }

    @Override
    public String getEventType() {
        return "message-summary";
    }
}
