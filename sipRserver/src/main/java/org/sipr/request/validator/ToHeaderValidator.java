package org.sipr.request.validator;

import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.core.sip.request.validator.RequestValidator;
import org.sipr.utils.SipUtils;
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

@Component
@Order(value = 1)
public class ToHeaderValidator implements RequestValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToHeaderValidator.class);

    @Inject
    SipUtils sipUtils;

    @Override
    public void validateRequest(RequestEvent requestEvent) throws RequestException {
        LOGGER.debug("Entering ToHeaderValidator");
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
