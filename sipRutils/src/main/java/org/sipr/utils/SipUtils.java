package org.sipr.utils;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.List;

public interface SipUtils {

    String getCallId(RequestEvent requestEvent);

    String getRequestMethod(RequestEvent requestEvent);

    URI getCanonicalizedURI(URI uri);

    String extractAuthUser(Request request);

    String extractToUser(Request request);

    String extractFromUser(Request request);

    String getSipURI(Address address);

    List<ContactHeader> extractContactHeaders(Request request);

    String extractUserAgent(Request request);

    boolean containsWildCardHeader(List<ContactHeader> headers);

    String getFirstContactUri(Request request);

    ContactHeader createProviderContactHeader(SipProvider sipProvider) throws ParseException;

    ExpiresHeader createExpiresHeader(int expires) throws InvalidArgumentException;

}
