package org.sipr.cassandra.dao;

import org.sipr.cassandra.domain.CassandraSubscriptionBinding;
import org.sipr.core.dao.SubscriptionBindingsRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CassandraSubscriptionsRepository extends SubscriptionBindingsRepository, CrudRepository<CassandraSubscriptionBinding, Long> {
    @Override
    @Query("select * from subscriptions where contact = ?0 and type = ?1 allow filtering")
    CassandraSubscriptionBinding findByContactAndType(@Param("contact") String contact, @Param("type") String type);
}
