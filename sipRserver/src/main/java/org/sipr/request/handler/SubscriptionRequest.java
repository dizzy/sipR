package org.sipr.request.handler;

import javax.sip.*;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class SubscriptionRequest {

    RequestEvent requestEvent;
    EventHeader eventHeader;
    SipProvider sipProvider;
    ExpiresHeader expiresHeader;
    long cSeq;
    String contactUri;
    String callId;
    String authUser;
    ServerTransaction serverTransaction;
    boolean isUnsubscribe;
    int response = Response.OK;

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(RequestEvent requestEvent, String authUser) {
        this.requestEvent = requestEvent;
        this.authUser = authUser;
    }

    public String getEventType() {
        return eventHeader.getEventType();
    }

    public SipProvider getSipProvider() {
        return sipProvider;
    }

    public ExpiresHeader getExpiresHeader() {
        return expiresHeader;
    }

    public EventHeader getEventHeader() {
        return eventHeader;
    }

    public long getCSeq() {
        return cSeq;
    }

    public String getContactUri() {
        return contactUri;
    }

    public String getCallId() {
        return callId;
    }

    public String getUser() {
        return authUser;
    }

    public int getResponse() {
        return response;
    }

    public Request getRequest() {
        return requestEvent.getRequest();
    }

    public boolean isInitialSubscribe() {
        return response == Response.ACCEPTED;
    }

    public ServerTransaction getServerTransaction() throws SipException {
        return serverTransaction;
    }

    public Dialog getDialog() throws SipException {
        Dialog dialog = requestEvent.getDialog();
        if (dialog == null) {
            dialog = serverTransaction.getDialog();
            dialog.terminateOnBye(false);
        }
        return dialog;
    }

    public boolean isUnsubscribe() {
        return isUnsubscribe;
    }

    public int getExpires() {
        return expiresHeader.getExpires();
    }
}
