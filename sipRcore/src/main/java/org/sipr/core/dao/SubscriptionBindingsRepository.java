package org.sipr.core.dao;

import java.util.List;

public interface SubscriptionBindingsRepository<SubscriptionBinding> {
    SubscriptionBinding findByContactAndType(String contact, String type);

    List<SubscriptionBinding> findByUsernameAndType(String user, String type);
}
