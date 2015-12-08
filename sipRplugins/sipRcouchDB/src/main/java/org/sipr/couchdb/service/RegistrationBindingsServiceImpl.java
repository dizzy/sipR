package org.sipr.couchdb.service;

import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.couchdb.dao.CouchDBRegistrationsRepository;
import org.sipr.couchdb.domain.CouchDBRegistrationBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Component
public class RegistrationBindingsServiceImpl implements RegistrationBindingsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBindingsServiceImpl.class);

    @Inject
    public CouchDBRegistrationsRepository registrationsRepository;

    @Override
    public void deleteAllBindings(String userName) {
        Long deleted = registrationsRepository.deleteByUserName(userName);
        LOGGER.debug(format("Deleted %s bindings for user %s", String.valueOf(deleted), userName));
    }

    @Override
    public void deleteBindings(List couchDBRegistrationBindings) {
        registrationsRepository.deleteBindings(couchDBRegistrationBindings);
    }

    @Override
    public void saveBindings(List couchDBRegistrationBindings) {
        registrationsRepository.saveBindings(couchDBRegistrationBindings);
    }

    @Override
    public Map<String, RegistrationBinding> findByUserName(String userName) {
        List<CouchDBRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (CouchDBRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(RegistrationBinding couchDBRegistrationBinding) {
        registrationsRepository.deleteBinding((CouchDBRegistrationBinding) couchDBRegistrationBinding);
    }

    @Override
    public void saveBinding(RegistrationBinding couchDBRegistrationBinding) {
        registrationsRepository.saveBinding((CouchDBRegistrationBinding) couchDBRegistrationBinding);
    }

    @Override
    public RegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires, String ua, String server) {
        CouchDBRegistrationBinding regBinding = new CouchDBRegistrationBinding(user, contactUri, callId, cseq, expires, ua, server);
        saveBinding(regBinding);
        return regBinding;
    }
}
