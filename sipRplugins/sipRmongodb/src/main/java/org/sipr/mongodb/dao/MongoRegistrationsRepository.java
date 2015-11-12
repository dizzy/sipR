package org.sipr.mongodb.dao;

import org.sipr.core.dao.RegistrationBindingsRepository;
import org.sipr.mongodb.domain.MongoRegistrationBinding;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRegistrationsRepository extends RegistrationBindingsRepository, MongoRepository<MongoRegistrationBinding, String> {
}
