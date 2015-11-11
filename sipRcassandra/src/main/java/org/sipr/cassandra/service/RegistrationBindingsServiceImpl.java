package org.sipr.cassandra.service;

import org.sipr.cassandra.dao.CassandraRegistrationsRepository;
import org.sipr.cassandra.domain.CassandraRegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Component
public class RegistrationBindingsServiceImpl implements RegistrationBindingsService<CassandraRegistrationBinding> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBindingsServiceImpl.class);

    @Inject
    CassandraRegistrationsRepository registrationsRepository;

    @Override
    public void deleteAllBindings(String userName) {
        Long deleted = registrationsRepository.deleteByUserName(userName);
        LOGGER.debug(format("Deleted %s bindings for user %s", String.valueOf(deleted), userName));
    }

    @Override
    public void deleteBindings(List<CassandraRegistrationBinding> bindings) {
        registrationsRepository.delete(bindings);
    }

    @Override
    public void saveBindings(List<CassandraRegistrationBinding> bindings) {
        registrationsRepository.save(bindings);
    }

    @Override
    public Map<String, CassandraRegistrationBinding> findByUserName(String userName) {
        List<CassandraRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (CassandraRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(CassandraRegistrationBinding binding) {
        registrationsRepository.delete(Collections.singletonList(binding));
    }

    @Override
    public void saveBinding(CassandraRegistrationBinding binding) {
        registrationsRepository.save(binding);
    }

    @Override
    public CassandraRegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires) {
        return new CassandraRegistrationBinding(user, contactUri, callId, cseq, expires);
    }
}
