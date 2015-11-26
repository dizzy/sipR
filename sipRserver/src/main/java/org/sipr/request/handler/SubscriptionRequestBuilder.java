package org.sipr.request.handler;

import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;

public interface SubscriptionRequestBuilder {

    SubscriptionRequest getSubscriptionRequest(RequestEvent requestEvent) throws InvalidArgumentException, SipException;
}
