package org.sipr.request.notify;

import org.sipr.core.domain.UserPresence;
import org.sipr.core.service.UserPresenceService;
import org.sipr.request.handler.SubscriptionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Request;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class DndContentBuilder implements NotifyContentBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DndContentBuilder.class);

    private static final Pattern DND_PATTERN = Pattern.compile("<doNotDisturbOn>(.+?)</doNotDisturbOn>");

    public static final String DND_EVENT = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
            "<DoNotDisturbEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\r\n" +
            "  <device>%s</device>\r\n" +
            "  <doNotDisturbOn>%s</doNotDisturbOn>\r\n" +
            "</DoNotDisturbEvent>\r\n";

    @Inject
    HeaderFactory headerFactory;

    @Inject
    UserPresenceService presenceService;

    @Inject
    NotifySender sender;

    @Override
    public void addContent(Request notifyRequest, SubscriptionRequest request) throws ParseException {
        String content = new String(request.getRequest().getRawContent(), StandardCharsets.UTF_8);
        String user = request.getUser();

        UserPresence presence = presenceService.getPresence(user);
        boolean sendToAll = false;
        boolean dnd = false;
        if (isNotEmpty(content)) {
            Matcher matcher = DND_PATTERN.matcher(content);
            if (matcher.find()) {
                dnd = Boolean.valueOf(matcher.group(1));
            }

            if (dnd) {
                presence.setPresence(UserPresence.DND);
            } else {
                presence.setPresence(UserPresence.AVAILABLE);
            }
            presenceService.savePresence(presence);

            sendToAll = true;

        } else {
            dnd = presence.isDndEnabled();
        }

        String responseContent = String.format(DND_EVENT, user, dnd);
        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "x-as-feature-event+xml");
        notifyRequest.setContent(responseContent, contentTypeHeader);

        //trigger notifies for all contacts but this one
        if (sendToAll) {
            sender.sendNotifyToAllSubscribers(request, contentTypeHeader, responseContent);
        }
    }

    @Override
    public String getEventType() {
        return "as-feature-event";
    }
}
