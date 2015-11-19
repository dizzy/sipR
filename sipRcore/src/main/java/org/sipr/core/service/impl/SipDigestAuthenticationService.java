package org.sipr.core.service.impl;

import org.sipr.core.domain.AuthDetails;
import org.sipr.core.service.AuthenticationService;
import org.sipr.core.service.SipAuthenticationService;
import org.sipr.core.utils.SipUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import java.text.ParseException;

@Component
public class SipDigestAuthenticationService implements SipAuthenticationService {

    @Inject
    SipUtils sipUtils;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    HeaderFactory headerFactory;

    @Override
    public boolean authenticateRequest(Request request) {
        return authenticationService.authenticate(createAuthDetails(request), sipUtils.extractAuthUser(request));
    }

    @Override
    public WWWAuthenticateHeader createAuthHeader() throws ParseException {
        WWWAuthenticateHeader wwwAuthenticateHeader = headerFactory.createWWWAuthenticateHeader("Digest");
        wwwAuthenticateHeader.setParameter("realm", authenticationService.getRealm());
        wwwAuthenticateHeader.setParameter("nonce", authenticationService.generateNonce());
        wwwAuthenticateHeader.setParameter("opaque", "");
        wwwAuthenticateHeader.setParameter("stale", "FALSE");
        wwwAuthenticateHeader.setParameter("algorithm", authenticationService.getAlgorithm());
        return wwwAuthenticateHeader;
    }

    AuthDetails createAuthDetails(Request request) {
        AuthorizationHeader authorizationHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
        String username = authorizationHeader.getUsername();
        String realm = authorizationHeader.getRealm();
        String uri = authorizationHeader.getURI().toString();
        String algorithm = authorizationHeader.getAlgorithm();
        String nonce = authorizationHeader.getNonce();
        String cnonce = authorizationHeader.getCNonce();
        String response = authorizationHeader.getResponse();
        String method = request.getMethod();
        return new DigestAuthDetails(username, realm, uri, algorithm, nonce, cnonce, response, method);
    }
}
