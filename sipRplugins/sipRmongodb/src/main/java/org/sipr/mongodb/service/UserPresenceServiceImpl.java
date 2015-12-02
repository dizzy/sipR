package org.sipr.mongodb.service;

import org.sipr.core.domain.UserPresence;
import org.sipr.core.service.UserPresenceService;
import org.sipr.mongodb.dao.MongoUserPresenceRepository;
import org.sipr.mongodb.domain.MongoUserPresence;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserPresenceServiceImpl implements UserPresenceService {

    @Inject
    MongoUserPresenceRepository repository;

    @Override
    public UserPresence getPresence(String user) {
        UserPresence presence = (MongoUserPresence) repository.findByUserName(user);
        if (presence == null) {
            presence = repository.save(new MongoUserPresence(user, UserPresence.AVAILABLE));
        }
        return presence;
    }

    @Override
    public void savePresence(UserPresence presence) {
        repository.save((MongoUserPresence) presence);
    }

    @Override
    public UserPresence createPresence(String username, String presence) {
        return new MongoUserPresence(username, presence);
    }
}
