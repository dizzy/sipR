package org.sipr.mongodb.service;

import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.mongodb.dao.MongoRegistrationsRepository;
import org.sipr.mongodb.domain.MongoRegistrationBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

@Component
public class RegistrationBindingsServiceImpl implements RegistrationBindingsService<MongoRegistrationBinding> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBindingsServiceImpl.class);

    @Inject
    MongoRegistrationsRepository registrationsRepository;


    @Override
    public void deleteAllBindings(String userName) {
        Long deleted = registrationsRepository.deleteByUserName(userName);
        LOGGER.debug(format("Deleted %s bindings for user %s", String.valueOf(deleted), userName));
    }

    @Override
    public void deleteBindings(List<MongoRegistrationBinding> bindings) {
        registrationsRepository.delete(bindings);
    }

    @Override
    public void saveBindings(List<MongoRegistrationBinding> bindings) {
        registrationsRepository.save(bindings);
    }

    @Override
    public Map<String, MongoRegistrationBinding> findByUserName(String userName) {
        List<MongoRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (MongoRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(MongoRegistrationBinding binding) {
        registrationsRepository.delete(Collections.singletonList(binding));
    }

    @Override
    public void saveBinding(MongoRegistrationBinding binding) {
        registrationsRepository.save(binding);
    }

    @Override
    public MongoRegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires) {
        return new MongoRegistrationBinding(user, contactUri, callId, cseq, expires);
    }
}