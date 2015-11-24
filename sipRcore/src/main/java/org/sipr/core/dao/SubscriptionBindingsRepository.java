package org.sipr.core.dao;

public interface SubscriptionBindingsRepository<SubscriptionBinding> {
    SubscriptionBinding findByContactAndType(String contact, String type);
}
