package org.sipr.mongodb.service;

import org.sipr.core.domain.SubscriptionBinding;
import org.sipr.core.service.SubscriptionBindingsService;
import org.sipr.mongodb.dao.MongoSubscriptionsRepository;
import org.sipr.mongodb.domain.MongoSubscriptionBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
public class SubscriptionBindingsServiceImpl implements SubscriptionBindingsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionBindingsServiceImpl.class);

    @Inject
    MongoSubscriptionsRepository subscriptionsRepository;

    @Override
    public MongoSubscriptionBinding findByContactAndType(String contact, String type) {
        return (MongoSubscriptionBinding) subscriptionsRepository.findByContactAndType(contact, type);
    }

    @Override
    public void deleteSubscription(SubscriptionBinding subscription) {
        subscriptionsRepository.delete((MongoSubscriptionBinding) subscription);
    }

    @Override
    public void saveSubscription(SubscriptionBinding subscription) {
        subscriptionsRepository.save((MongoSubscriptionBinding) subscription);
    }

    @Override
    public List<SubscriptionBinding> findByUserNameAndType(String userName, String type) {
        return (List<SubscriptionBinding>)(List<?>) subscriptionsRepository.findByUserNameAndType(userName, type);
    }

    @Override
    public SubscriptionBinding createSubscription(String user, String contactUri, String callId, long cseq, int expires, String type) {
        return new MongoSubscriptionBinding(user, contactUri, callId, cseq, expires, type);
    }
}
