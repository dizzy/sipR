package org.sipr.request.validator;

import org.sipr.request.processor.RequestException;

import javax.sip.RequestEvent;
import java.text.ParseException;

public interface RequestValidator {
    void validateRequest(RequestEvent requestEvent) throws RequestException, ParseException;
}
