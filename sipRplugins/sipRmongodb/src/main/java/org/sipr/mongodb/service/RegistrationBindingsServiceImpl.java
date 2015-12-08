package org.sipr.mongodb.service;

import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.mongodb.dao.MongoRegistrationsRepository;
import org.sipr.mongodb.domain.MongoRegistrationBinding;
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
    MongoRegistrationsRepository registrationsRepository;


    @Override
    public void deleteAllBindings(String userName) {
        Long deleted = registrationsRepository.deleteByUserName(userName);
        LOGGER.debug(format("Deleted %s bindings for user %s", String.valueOf(deleted), userName));
    }

    @Override
    public void deleteBindings(List bindings) {
        registrationsRepository.delete(bindings);
    }

    @Override
    public void saveBindings(List bindings) {
        registrationsRepository.save(bindings);
    }

    @Override
    public Map<String, RegistrationBinding> findByUserName(String userName) {
        List<MongoRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (MongoRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(RegistrationBinding binding) {
        registrationsRepository.delete((MongoRegistrationBinding) binding);
    }

    @Override
    public void saveBinding(RegistrationBinding binding) {
        registrationsRepository.save((MongoRegistrationBinding) binding);
    }

    @Override
    public RegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires, String ua, String server) {
        return new MongoRegistrationBinding(user, contactUri, callId, cseq, expires, ua, server);
    }
}
