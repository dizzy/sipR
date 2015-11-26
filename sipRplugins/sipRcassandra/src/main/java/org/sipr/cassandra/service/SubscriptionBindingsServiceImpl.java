package org.sipr.cassandra.service;

import org.sipr.cassandra.dao.CassandraSubscriptionsRepository;
import org.sipr.cassandra.domain.CassandraSubscriptionBinding;
import org.sipr.core.domain.SubscriptionBinding;
import org.sipr.core.service.SubscriptionBindingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.WriteOptions;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SubscriptionBindingsServiceImpl implements SubscriptionBindingsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionBindingsServiceImpl.class);

    @Inject
    CassandraSubscriptionsRepository subscriptionsRepository;

    @Autowired
    CassandraTemplate cassandraTemplate;

    @Override
    public CassandraSubscriptionBinding findByContactAndType(String contact, String type) {
        return subscriptionsRepository.findByContactAndType(contact, type);
    }

    @Override
    public void deleteSubscription(SubscriptionBinding subscription) {
        subscriptionsRepository.delete((CassandraSubscriptionBinding) subscription);
    }

    @Override
    public void saveSubscription(SubscriptionBinding subscription) {
        // TODO use TTL repository support when available
        // workaround lack of TTL support in spring data repository, use template to insert with TTL
        WriteOptions options = new WriteOptions();
        options.setTtl(subscription.getExpires() + 5);
        cassandraTemplate.insert(subscription, options);
    }

    @Override
    public CassandraSubscriptionBinding createSubscription(String user, String contactUri, String callId, long cseq, int expires, String type) {
        return new CassandraSubscriptionBinding(user, contactUri, callId, cseq, expires, type);
    }
}
