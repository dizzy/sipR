package org.sipr.request.handler.impl;

import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.core.service.SipAuthenticationService;
import org.sipr.request.processor.RequestException;
import org.sipr.request.handler.RegistrationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.RequestEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.*;

import static org.apache.commons.collections4.IteratorUtils.toList;

@Component
public class RegistrationHandlerImpl implements RegistrationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationHandlerImpl.class);

    @Value("${sip.registration.expires}")
    private int serverExpire;

    @Inject
    RegistrationBindingsService registrationService;

    @Inject
    SipAuthenticationService sipAuthenticationService;

    @Override
    public Collection<RegistrationBinding> handleRequest(RequestEvent requestEvent) throws RequestException {
        Request request = requestEvent.getRequest();
        String user = sipAuthenticationService.extractUserFromToHeader(request);
        List<ContactHeader> headers = extractContactHeaders(request);
        List<RegistrationBinding> bindingsToDelete = new ArrayList<>();
        List<RegistrationBinding> bindingsToSave = new ArrayList<>();
        Map<String, RegistrationBinding> currentBindings = registrationService.findByUserName(user);

        ExpiresHeader expiresHeader = (ExpiresHeader) request.getHeader(ExpiresHeader.NAME);

        boolean deleteAllBindings = false;
        if (containsWildCardHeader(headers)) {

            if (headers.size() != 1 || expiresHeader == null || expiresHeader.getExpires() != 0) {
                throw new RequestException(Response.BAD_REQUEST);
            }
            deleteAllBindings = true;

        } else {
            for (ContactHeader header : headers) {
                String contactUri = header.getAddress().getURI().toString();
                RegistrationBinding binding = currentBindings.get(contactUri);

                String callId = ((CallIdHeader) request.getHeader(CallIdHeader.NAME)).getCallId();
                long cseq = ((CSeqHeader) request.getHeader(CSeqHeader.NAME)).getSeqNumber();
                int expires = header.getExpires();

                if (binding != null) {
                    if (callId.equals(binding.getCallId()) && cseq <= binding.getCseq()) {
                        throw new RequestException(Response.BAD_REQUEST);
                    }

                    if (expires == 0 || expiresHeader.getExpires() == 0) {
                        bindingsToDelete.add(binding);
                        currentBindings.remove(binding.getContact());
                    } else {
                        if (expires == -1) {
                            expires = serverExpire;
                        }
                        binding.setCseq(cseq);
                        binding.setExpires(expires);
                        bindingsToSave.add(binding);
                    }
                } else {
                    if (expires == -1) {
                        expires = serverExpire;
                    }
                    RegistrationBinding newBinding = (RegistrationBinding) registrationService.createRegistrationBinding(user, contactUri, callId, cseq, expires);
                    bindingsToSave.add(newBinding);
                    currentBindings.put(newBinding.getContact(), newBinding);
                }
            }
        }

        if (deleteAllBindings) {
            registrationService.deleteAllBindings(user);
        } else {
            if (!bindingsToDelete.isEmpty()) {
                registrationService.deleteBindings(bindingsToDelete);
            }
        }

        if (!bindingsToSave.isEmpty()) {
            registrationService.saveBindings(bindingsToSave);
        }

        return currentBindings.values();
    }

    List<ContactHeader> extractContactHeaders(Request request) {
        ListIterator<ContactHeader> headers = request.getHeaders(ContactHeader.NAME);
        if (headers != null) {
            return toList(headers);
        }
        return null;
    }

    boolean containsWildCardHeader(List<ContactHeader> headers) {
        if (headers != null) {
            for (ContactHeader contactHeader : headers) {
                if (contactHeader.isWildCard()) {
                    return true;
                }
            }
        }
        return false;
    }
}
