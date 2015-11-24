package org.sipr.core.service;

import java.util.List;

public interface SubscriptionBindingsService<SubscriptionBinding> {

    SubscriptionBinding findByContactAndType(String contact, String type);

    void deleteSubscription(SubscriptionBinding subscription);

    void saveSubscription(SubscriptionBinding subscription);

    SubscriptionBinding createSubscription(String user, String contactUri, String callId, long cseq, int expires, String type);
}
