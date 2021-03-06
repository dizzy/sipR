package org.sipr.core.sip.request.handler;

import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.sip.request.processor.RequestException;

import javax.sip.RequestEvent;
import java.util.Collection;

public interface RegistrationHandler {
    Collection<RegistrationBinding> handleRequest(RequestEvent requestEvent) throws RequestException;
}
