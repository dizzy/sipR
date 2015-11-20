package org.sipr.utils;

import gov.nist.javax.sip.address.SipUri;
import org.springframework.stereotype.Component;

import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.util.List;
import java.util.ListIterator;

import static org.apache.commons.collections4.IteratorUtils.toList;

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

    public String extractAuthUser(Request request) {
        if (request.getMethod().equalsIgnoreCase(Request.REGISTER)) {
            return extractToUser(request);
        }
        return extractFromUser(request);
    }

    public String extractToUser(Request request) {
        ToHeader header = (ToHeader) request.getHeader(ToHeader.NAME);
        return getSipURI(header.getAddress());
    }

    public String extractFromUser(Request request) {
        FromHeader header = (FromHeader) request.getHeader(ToHeader.NAME);
        return getSipURI(header.getAddress());
    }

    private String getSipURI(Address address) {
        SipUri sipUri = (SipUri) getCanonicalizedURI(address.getURI());
        sipUri.clearPassword();
        sipUri.removePort();
        sipUri.clearQheaders();
        return sipUri.getUser();
    }


    public List<ContactHeader> extractContactHeaders(Request request) {
        ListIterator<ContactHeader> headers = request.getHeaders(ContactHeader.NAME);
        if (headers != null) {
            return toList(headers);
        }
        return null;
    }

    public boolean containsWildCardHeader(List<ContactHeader> headers) {
        if (headers != null) {
            for (ContactHeader contactHeader : headers) {
                if (contactHeader.isWildCard()) {
                    return true;
                }
            }
        }
        return false;
    }
}
