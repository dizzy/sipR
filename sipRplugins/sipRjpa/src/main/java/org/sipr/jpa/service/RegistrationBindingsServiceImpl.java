package org.sipr.jpa.service;

import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.jpa.dao.JpaRegistrationsRepository;
import org.sipr.jpa.domain.JpaRegistrationBinding;
import org.sipr.jpa.domain.JpaUser;
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
public class RegistrationBindingsServiceImpl implements RegistrationBindingsService<JpaRegistrationBinding> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBindingsServiceImpl.class);

    @Inject
    JpaRegistrationsRepository registrationsRepository;

    @Override
    public void deleteAllBindings(String userName) {
        Long deleted = registrationsRepository.deleteByUserName(userName);
        LOGGER.debug(format("Deleted %s bindings for user %s", String.valueOf(deleted), userName));
    }

    @Override
    public void deleteBindings(List<JpaRegistrationBinding> bindings) {
        registrationsRepository.delete(bindings);
    }

    @Override
    public void saveBindings(List<JpaRegistrationBinding> bindings) {
        registrationsRepository.save(bindings);
    }

    @Override
    public Map<String, JpaRegistrationBinding> findByUserName(String userName) {
        List<JpaRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (JpaRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(JpaRegistrationBinding binding) {
        registrationsRepository.delete(Collections.singletonList(binding));
    }

    @Override
    public void saveBinding(JpaRegistrationBinding binding) {
        registrationsRepository.save(binding);
    }

    @Override
    public JpaRegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires) {
        return new JpaRegistrationBinding(user, contactUri, callId, cseq, expires);
    }
}
