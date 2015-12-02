package org.sipr.core.dao;

import java.util.List;

public interface SubscriptionBindingsRepository<SubscriptionBinding> {
    SubscriptionBinding findByContactAndType(String contact, String type);

    List<SubscriptionBinding> findByUserNameAndType(String user, String type);
}
