package org.sipr.registrar.request.validator;

import org.sipr.registrar.request.handler.RequestException;

import javax.sip.RequestEvent;
import java.text.ParseException;

public interface RequestValidator {
    void validateRequest(RequestEvent requestEvent) throws RequestException, ParseException;
}
