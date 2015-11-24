package org.sipr.core.domain;

public interface BaseBinding {

    String getUserName();

    void setUserName(String userName);

    String getContact();

    void setContact(String contact);

    String getCallId();

    void setCallId(String callId);

    long getCseq();

    void setCseq(long cseq);

    int getExpires();

    void setExpires(int expires);
}
