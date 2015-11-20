package org.sipr.core.sip.request.validator;

import org.sipr.core.sip.request.processor.RequestException;

import javax.sip.RequestEvent;
import java.text.ParseException;

public interface RequestValidator {
    void validateRequest(RequestEvent requestEvent) throws RequestException, ParseException;
}
