package org.sipr.couchdb.service;

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
import static java.util.stream.Collectors.toMap;

@Component
public class RegistrationBindingsServiceImpl implements RegistrationBindingsService<CouchDBRegistrationBinding> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBindingsServiceImpl.class);

    @Inject
    public CouchDBRegistrationsRepository registrationsRepository;

    @Override
    public void deleteAllBindings(String userName) {
        Long deleted = registrationsRepository.deleteByUserName(userName);
        LOGGER.debug(format("Deleted %s bindings for user %s", String.valueOf(deleted), userName));
    }

    @Override
    public void deleteBindings(List<CouchDBRegistrationBinding> couchDBRegistrationBindings) {
        registrationsRepository.deleteBindings(couchDBRegistrationBindings);
    }

    @Override
    public void saveBindings(List<CouchDBRegistrationBinding> couchDBRegistrationBindings) {
        registrationsRepository.saveBindings(couchDBRegistrationBindings);
    }

    @Override
    public Map<String, CouchDBRegistrationBinding> findByUserName(String userName) {
        List<CouchDBRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (CouchDBRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(CouchDBRegistrationBinding couchDBRegistrationBinding) {
        registrationsRepository.deleteBinding(couchDBRegistrationBinding);
    }

    @Override
    public void saveBinding(CouchDBRegistrationBinding couchDBRegistrationBinding) {
        registrationsRepository.saveBinding(couchDBRegistrationBinding);
    }

    @Override
    public CouchDBRegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires) {
        CouchDBRegistrationBinding regBinding = new CouchDBRegistrationBinding(user, contactUri, callId, cseq, expires);
        saveBinding(regBinding);
        return regBinding;
    }
}
