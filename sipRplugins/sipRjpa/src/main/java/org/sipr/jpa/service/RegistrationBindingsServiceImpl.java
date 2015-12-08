package org.sipr.jpa.service;

import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.jpa.dao.JpaRegistrationsRepository;
import org.sipr.jpa.domain.JpaRegistrationBinding;
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
    JpaRegistrationsRepository registrationsRepository;

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
        List<JpaRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (JpaRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(RegistrationBinding binding) {
        registrationsRepository.delete((JpaRegistrationBinding) binding);
    }

    @Override
    public void saveBinding(RegistrationBinding binding) {
        registrationsRepository.save((JpaRegistrationBinding) binding);
    }

    @Override
    public RegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires, String ua, String server) {
        return new JpaRegistrationBinding(user, contactUri, callId, cseq, expires, ua, server);
    }
}
