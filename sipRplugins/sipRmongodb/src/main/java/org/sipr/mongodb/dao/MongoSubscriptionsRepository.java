package org.sipr.mongodb.dao;

import org.sipr.core.dao.SubscriptionBindingsRepository;
import org.sipr.mongodb.domain.MongoSubscriptionBinding;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoSubscriptionsRepository extends SubscriptionBindingsRepository, MongoRepository<MongoSubscriptionBinding, String> {
}
