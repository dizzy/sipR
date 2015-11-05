package org.sipr.core.service;

import javax.sip.RequestEvent;
import javax.sip.header.Header;
import java.util.List;

public interface SipMessageSender {
    void sendResponse(RequestEvent requestEvent, Integer responseCode);

    void sendResponse(RequestEvent requestEvent, Integer responseCode, List<? extends Header> headers);
}
