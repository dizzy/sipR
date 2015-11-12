package org.sipr.couchdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ektorp.support.CouchDbDocument;
import org.sipr.core.domain.RegistrationBinding;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouchDBRegistrationBinding extends CouchDbDocument implements RegistrationBinding {

    String userName;

    String contact;

    String callId;

    long cseq;

    int expires;

    public CouchDBRegistrationBinding() {

    }

    public CouchDBRegistrationBinding(String userName, String contact, String callId, long cseq, int expires) {
        this.userName = userName;
        this.contact = contact;
        this.callId = callId;
        this.cseq = cseq;
        this.expires = expires;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getContact() {
        return contact;
    }

    @Override
    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String getCallId() {
        return callId;
    }

    @Override
    public void setCallId(String callId) {
        this.callId = callId;
    }

    @Override
    public long getCseq() {
        return cseq;
    }

    @Override
    public void setCseq(long cseq) {
        this.cseq = cseq;
    }

    @Override
    public int getExpires() {
        return expires;
    }

    @Override
    public void setExpires(int expires) {
        this.expires = expires;
    }
}
