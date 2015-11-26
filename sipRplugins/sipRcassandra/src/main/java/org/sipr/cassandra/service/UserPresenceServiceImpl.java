package org.sipr.cassandra.service;

import org.sipr.cassandra.dao.CassandraUserPresenceRepository;
import org.sipr.cassandra.domain.CassandraUserPresence;
import org.sipr.core.domain.UserPresence;
import org.sipr.core.service.UserPresenceService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserPresenceServiceImpl implements UserPresenceService {

    @Inject
    CassandraUserPresenceRepository repository;

    @Override
    public UserPresence getPresence(String user) {
        return repository.findByUserName(user);
    }

    @Override
    public void savePresence(UserPresence presence) {
        repository.save((CassandraUserPresence) presence);
    }

    @Override
    public UserPresence createPresence(String username, String presence) {
        return new CassandraUserPresence(username, presence);
    }
}
