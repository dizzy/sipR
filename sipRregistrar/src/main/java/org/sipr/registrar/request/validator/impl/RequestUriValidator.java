package org.sipr.registrar.request.validator.impl;

import org.sipr.core.service.SipDomainService;
import org.sipr.core.utils.SipUtils;
import org.sipr.registrar.request.handler.RequestException;
import org.sipr.registrar.request.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Component
@Order(value = 0)
public class RequestUriValidator implements RequestValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUriValidator.class);

    @Inject
    SipUtils sipUtils;

    @Inject
    SipDomainService sipDomainService;

    @Override
    public void validateRequest(RequestEvent requestEvent) throws RequestException {
        LOGGER.debug("RequestUriValidator for registrar");
        Request request = requestEvent.getRequest();
        URI uri = sipUtils.getCanonicalizedURI(request.getRequestURI());

        if (uri == null || !uri.isSipURI()) {
            throw new RequestException(Response.BAD_REQUEST);
        }

        String requestDomain = ((SipURI) uri).getHost();
        if (!sipDomainService.isValidDomain(requestDomain)) {
            throw new RequestException(Response.BAD_REQUEST);
        }
    }
}
