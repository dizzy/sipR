package org.sipr.cassandra.service;

import org.sipr.cassandra.dao.CassandraRegistrationsRepository;
import org.sipr.cassandra.domain.CassandraRegistrationBinding;
import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Component
public class RegistrationBindingsServiceImpl implements RegistrationBindingsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBindingsServiceImpl.class);

    @Inject
    CassandraRegistrationsRepository registrationsRepository;

    @Autowired
    CassandraTemplate cassandraTemplate;

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
    public void saveBindings(List<RegistrationBinding> bindings) {
        for (RegistrationBinding binding : bindings) {
            saveBinding(binding);
        }
    }

    @Override
    public Map<String, RegistrationBinding> findByUserName(String userName) {
        List<CassandraRegistrationBinding> bindings = registrationsRepository.findByUserName(userName);
        Map registrations = new HashMap<>();
        for (CassandraRegistrationBinding binding : bindings) {
            registrations.put(binding.getContact(), binding);
        }
        return registrations;
    }

    @Override
    public void deleteBinding(RegistrationBinding binding) {
        registrationsRepository.delete((CassandraRegistrationBinding) binding);
    }

    @Override
    public void saveBinding(RegistrationBinding binding) {
        // TODO use TTL repository support when available
        // workaround lack of TTL support in spring data repository, use template to insert with TTL
        WriteOptions options = new WriteOptions();
        options.setTtl(binding.getExpires() + 5);
        cassandraTemplate.insert(binding, options);
    }

    @Override
    public RegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires) {
        return new CassandraRegistrationBinding(user, contactUri, callId, cseq, expires);
    }
}
