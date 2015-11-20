package org.sipr.request.validator;

import org.sipr.core.service.SipAuthenticationService;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.core.sip.request.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.header.AuthorizationHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Collections;

@Component
@Order(value = 3)
public class AuthValidator implements RequestValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthValidator.class);

    @Inject
    SipAuthenticationService sipAuthenticationService;

    @Override
    public void validateRequest(RequestEvent requestEvent) throws RequestException, ParseException {
        LOGGER.debug("Entering AuthValidator");
        Request request = requestEvent.getRequest();
        AuthorizationHeader authorizationHeader = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);

        if (authorizationHeader == null) {
            throw new RequestException(Response.UNAUTHORIZED, Collections.singletonList(sipAuthenticationService.createAuthHeader()));
        } else {
            if (!sipAuthenticationService.authenticateRequest(request)) {
                throw new RequestException(Response.NOT_FOUND);
            }
        }
    }
}
