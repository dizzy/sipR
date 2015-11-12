package org.sipr.mongodb.dao;

import org.sipr.core.dao.UserRepository;
import org.sipr.mongodb.domain.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserRepository extends UserRepository, MongoRepository<MongoUser, String> {
}
