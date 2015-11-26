package org.sipr.request.handler;

import org.sipr.utils.SipUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.*;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Response;

@Component
public class SubscriptionRequestBuilder {
    @Inject
    SipUtils sipUtils;

    @Value("${sip.subscription.expires}")
    int serverExpire;

    @Value("${sip.force.subscription.expires}")
    private boolean forceServerExpire;

    public SubscriptionRequest getSubscriptionRequest(RequestEvent requestEvent) throws InvalidArgumentException, SipException {
        SubscriptionRequest request = new SubscriptionRequest();
        request.requestEvent = requestEvent;
        request.sipProvider = (SipProvider) requestEvent.getSource();
        request.eventHeader = (EventHeader) requestEvent.getRequest().getHeader(EventHeader.NAME);
        request.cSeq = ((CSeqHeader) requestEvent.getRequest().getHeader(CSeqHeader.NAME)).getSeqNumber();
        request.callId = ((CallIdHeader) requestEvent.getRequest().getHeader(CallIdHeader.NAME)).getCallId();
        request.contactUri = sipUtils.getFirstContactUri(requestEvent.getRequest());
        request.authUser = sipUtils.extractAuthUser(requestEvent.getRequest());

        if (requestEvent.getDialog() == null) {
            request.response = Response.ACCEPTED;
        }

        ExpiresHeader expires = (ExpiresHeader)  requestEvent.getRequest().getHeader(ExpiresHeader.NAME);
        if (expires != null) {
            request.isUnsubscribe = expires.getExpires() == 0;
        }

        if (expires == null) {
            expires = sipUtils.createExpiresHeader(serverExpire);
        }
        request.expiresHeader = expires;

        ServerTransaction st = requestEvent.getServerTransaction();
        if (st == null) {
            st = request.sipProvider.getNewServerTransaction(requestEvent.getRequest());
        }
        request.serverTransaction = st;
        return request;
    }
}
