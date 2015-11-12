package org.sipr.request.validator;

import org.sipr.request.handler.RequestException;

import javax.sip.RequestEvent;
import java.text.ParseException;

public interface RequestValidator {
    void validateRequest(RequestEvent requestEvent) throws RequestException, ParseException;
}
