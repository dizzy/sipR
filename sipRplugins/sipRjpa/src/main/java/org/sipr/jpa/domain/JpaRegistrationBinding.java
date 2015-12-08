package org.sipr.jpa.domain;

import org.sipr.core.domain.RegistrationBinding;

import javax.persistence.*;

@Entity
@Table(name = "registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames={"contact"})
})
public class JpaRegistrationBinding implements RegistrationBinding {

    @Id
    @GeneratedValue
    private Long id;

    String userName;
    String contact;
    String callId;
    Long cSeq;
    Integer expires;
    String ua;

    public JpaRegistrationBinding() {
    }

    public JpaRegistrationBinding(String userName, String contact, String callId, long cseq, int expires, String ua) {
        this.userName = userName;
        this.contact = contact;
        this.callId = callId;
        this.cSeq = cseq;
        this.expires = expires;
    }
    public String getUserName() {
        return userName;
    }

    public long getCseq() {
        return cSeq;
    }

    @Override
    public void setCseq(long cseq) {
        this.cSeq = cseq;
    }

    public int getExpires() {
        return expires;
    }

    @Override
    public void setExpires(int expires) {
        this.expires = expires;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    @Override
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCallId() {
        return callId;
    }

    @Override
    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}
