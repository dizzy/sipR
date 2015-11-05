package org.sipr.core.service;

import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import java.text.ParseException;

public interface SipAuthenticationService {
    boolean authenticateRequest(Request request);

    WWWAuthenticateHeader createAuthHeader() throws ParseException;

    String extractUserFromToHeader(Request request);
}
