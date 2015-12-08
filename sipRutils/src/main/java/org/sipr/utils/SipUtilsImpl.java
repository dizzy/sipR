package org.sipr.utils;

import gov.nist.javax.sip.address.SipUri;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.List;
import java.util.ListIterator;

import static org.apache.commons.collections4.IteratorUtils.toList;
import static org.apache.commons.lang3.StringUtils.*;

@Component
public class SipUtilsImpl implements SipUtils {

    @Inject
    AddressFactory addressFactory;

    @Inject
    HeaderFactory headerFactory;

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
        if (request.getMethod().equalsIgnoreCase(Request.REGISTER)
                || request.getMethod().equalsIgnoreCase(Request.SUBSCRIBE)) {
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

    public String getSipURI(Address address) {
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

    @Override
    public String extractUserAgent(Request request) {
        UserAgentHeader uaHeader = (UserAgentHeader) request.getHeader(UserAgentHeader.NAME);
        if (uaHeader != null) {
            return join(uaHeader.getProduct(), ";");
        }
        return EMPTY;
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

    public String getFirstContactUri(Request request) {
        List<ContactHeader> headers = extractContactHeaders(request);
        return headers.get(0).getAddress().getURI().toString();
    }

    public ContactHeader createProviderContactHeader(SipProvider sipProvider) throws ParseException {
        ListeningPoint listeningPoint = sipProvider.getListeningPoints()[0];
        SipURI uri = addressFactory.createSipURI(null, listeningPoint.getIPAddress());
        uri.setPort(listeningPoint.getPort());
        Address address = addressFactory.createAddress(uri);
        return headerFactory.createContactHeader(address);
    }

    public ExpiresHeader createExpiresHeader(int expires) throws InvalidArgumentException {
        return headerFactory.createExpiresHeader(expires);
    }

}
