package org.sipr.request.validator;

import org.sipr.core.service.SipDomainService;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.core.sip.request.validator.RequestValidator;
import org.sipr.utils.SipUtils;
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
        LOGGER.debug("Entering RequestUriValidator");
        Request request = requestEvent.getRequest();
        URI uri = sipUtils.getCanonicalizedURI(request.getRequestURI());

        if (uri == null || !uri.isSipURI()) {
            throw new RequestException(Response.BAD_REQUEST);
        }

        String requestDomain = ((SipURI) uri).getHost();
        LOGGER.debug("RequestUriValidator host: " + requestDomain);
        if (!sipDomainService.isValidDomain(requestDomain)) {
            throw new RequestException(Response.BAD_REQUEST);
        }
    }
}
