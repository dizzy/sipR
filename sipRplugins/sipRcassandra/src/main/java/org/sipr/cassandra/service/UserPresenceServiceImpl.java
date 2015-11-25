package org.sipr.cassandra.service;

import org.sipr.cassandra.dao.CassandraUserPresenceRepository;
import org.sipr.cassandra.domain.CassandraUserPresence;
import org.sipr.core.service.UserPresenceService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserPresenceServiceImpl implements UserPresenceService<CassandraUserPresence> {

    @Inject
    CassandraUserPresenceRepository repository;

    @Override
    public CassandraUserPresence getPresence(String user) {
        return repository.findByUserName(user);
    }

    @Override
    public void savePresence(CassandraUserPresence presence) {
        repository.save(presence);
    }

    @Override
    public CassandraUserPresence createPresence(String username, String presence) {
        return new CassandraUserPresence(username, presence);
    }
}
