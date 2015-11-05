package org.sipr.registrar.request.processor;

import org.sipr.core.domain.RegistrationBinding;
import org.sipr.registrar.request.handler.RequestException;

import javax.sip.RequestEvent;
import java.util.Collection;

public interface RequestProcessor {
    Collection<RegistrationBinding> processRequest(RequestEvent requestEvent) throws RequestException;
}
