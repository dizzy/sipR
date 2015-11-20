package org.sipr.collector.request.handler;

import org.sipr.core.sip.request.handler.PublishHandler;
import org.sipr.core.sip.request.processor.RequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.message.Request;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class VqrCollectorImpl implements PublishHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(VqrCollectorImpl.class);
    private static final Logger VQR_LOGGER = LoggerFactory.getLogger("vq-rtcpxr");

    @Override
    public void handleRequest(RequestEvent requestEvent) throws RequestException {
        Request publish = null;
        try {
            publish = requestEvent.getRequest();
            VQR_LOGGER.info(processContent(publish.getRawContent()));
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("Failed to store event: " + publish.toString());
        }
    }

    public String processContent(byte[] rawContent) throws UnsupportedEncodingException {
        String content = toEncodedString(rawContent, StandardCharsets.UTF_8);
        content = content.replaceAll("\\r\\n|\\r|\\n", ",");
        content = removeEnd(content, ",");
        content = appendIfMissing("ReportType:", content);
        content = remove(content, "\"");
        return content;
    }

    @Override
    public String getEventType() {
        return "vq-rtcpxr";
    }
}
