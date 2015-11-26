package org.sipr.core.service;

import org.sipr.core.domain.SubscriptionBinding;

import java.util.List;
import java.util.Map;

public interface SubscriptionBindingsService {

    <T extends SubscriptionBinding> T findByContactAndType(String contact, String type);

    void deleteSubscription(SubscriptionBinding subscription);

    void saveSubscription(SubscriptionBinding subscription);

    <T extends SubscriptionBinding> T createSubscription(String user, String contactUri, String callId, long cseq, int expires, String type);

    List<SubscriptionBinding> findByUserNameAndType(String userName, String type);
}
