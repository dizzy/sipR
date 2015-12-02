package org.sipr.mongodb.dao;

import org.sipr.core.dao.UserPresenceRepository;
import org.sipr.mongodb.domain.MongoUserPresence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserPresenceRepository extends UserPresenceRepository, MongoRepository<MongoUserPresence, String> {
}
