package org.sipr.core.utils;

import gov.nist.javax.sip.address.SipUri;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.address.URI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.Header;
import javax.sip.message.Request;

@Component
public class SipUtils {

    public String getCallId(RequestEvent requestEvent) {
        Request request = requestEvent.getRequest();
        Header callIdHeader = request.getHeader(CallIdHeader.NAME);
        return ((CallIdHeader) callIdHeader).getCallId();
    }

    public String getRequestMethod(RequestEvent requestEvent) {
        Request request = requestEvent.getRequest();
        return request.getMethod();
    }

    public URI getCanonicalizedURI(URI uri) {
        if (uri != null && uri.isSipURI()) {
            SipUri sipUri = (SipUri) uri.clone();
            sipUri.clearUriParms();
            return sipUri;
        }
        return uri;
    }
}
