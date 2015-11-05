package org.sipr.registrar.request.validator.impl;

import org.sipr.core.utils.SipUtils;
import org.sipr.registrar.request.handler.RequestException;
import org.sipr.registrar.request.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

@Component
@Order(value = 1)
public class ToHeaderValidator implements RequestValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToHeaderValidator.class);

    @Inject
    SipUtils sipUtils;

    @Override
    public void validateRequest(RequestEvent requestEvent) throws RequestException, ParseException {
        LOGGER.debug("ToHeaderValidator for registrar");
        Request request = requestEvent.getRequest();
        ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
        Address address = toHeader.getAddress();

        // check if TO Header is a SIP Uri
        URI addressUri = sipUtils.getCanonicalizedURI(address.getURI());
        if (!addressUri.isSipURI()) {
            throw new RequestException(Response.NOT_FOUND);
        }

        // Check if domain parts in To and RequestURI are equal
        URI requestUri = sipUtils.getCanonicalizedURI(request.getRequestURI());
        String requestUriHost = ((SipURI) requestUri).getHost();
        String addressUriHost = ((SipURI) addressUri).getHost();
        if (!addressUriHost.equalsIgnoreCase(requestUriHost)) {
            throw new RequestException(Response.NOT_FOUND);
        }
    }
}
