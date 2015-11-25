package org.sipr.request.notify;

import org.sipr.core.domain.UserPresence;
import org.sipr.core.service.UserPresenceService;
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

    private static final Pattern DND_PATTERN = Pattern.compile("<doNotDisturbOn>(.+?)</doNotDisturbOn>");

    private static final String DND_EVENT = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" +
            "<DoNotDisturbEvent xmlns=\"http://www.ecma-international.org/standards/ecma-323/csta/ed3\">\r\n" +
            "  <device>%s</device>\r\n" +
            "  <doNotDisturbOn>%s</doNotDisturbOn>\r\n" +
            "</DoNotDisturbEvent>\r\n";

    @Inject
    HeaderFactory headerFactory;

    @Inject
    UserPresenceService presenceService;

    @Override
    public void addContent(Request notifyRequest, String user, byte[] rawContent) throws ParseException {
        String content = new String(rawContent, StandardCharsets.UTF_8);

        UserPresence presence = (UserPresence) presenceService.getPresence(user);
        if (presence == null) {
            presence = (UserPresence) presenceService.createPresence(user, UserPresence.AVAILABLE);
            presenceService.savePresence(presence);
        }

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

        } else {
            dnd = presence.isDndEnabled();
        }

        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "x-as-feature-event+xml");
        notifyRequest.setContent(String.format(DND_EVENT, user, dnd), contentTypeHeader);
    }

    @Override
    public String getEventType() {
        return "as-feature-event";
    }
}
