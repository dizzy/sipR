package org.sipr.request.validator;

import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.core.sip.request.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ProxyRequireHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Collections;

@Component
@Order(value = 2)
public class RequireHeaderValidator implements RequestValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequireHeaderValidator.class);

    @Inject
    HeaderFactory headerFactory;

    @Override
    public void validateRequest(RequestEvent requestEvent) throws RequestException {
        LOGGER.debug("Enteirng RequireHeaderValidator");
        Request request = requestEvent.getRequest();
        ProxyRequireHeader requireHeader = (ProxyRequireHeader) request.getHeader(ProxyRequireHeader.NAME);
        if (requireHeader != null) {
            try {
                Header unsupportedHeader = headerFactory.createUnsupportedHeader(requireHeader.getOptionTag());
                throw new RequestException(Response.BAD_EXTENSION, Collections.singletonList(unsupportedHeader));
            } catch (ParseException pex) {
                throw new RequestException(Response.SERVER_INTERNAL_ERROR);
            }
        }
    }
}
