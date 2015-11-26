package org.sipr.core.service;

import org.sipr.core.domain.SubscriptionBinding;

public interface SubscriptionBindingsService {

    <T extends SubscriptionBinding> T findByContactAndType(String contact, String type);

    void deleteSubscription(SubscriptionBinding subscription);

    void saveSubscription(SubscriptionBinding subscription);

    <T extends SubscriptionBinding> T createSubscription(String user, String contactUri, String callId, long cseq, int expires, String type);
}
