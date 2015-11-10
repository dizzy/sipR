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

    @ManyToOne
    JpaUser user;

    String contact;
    String callId;
    Long cSeq;
    Integer expires;

    public JpaRegistrationBinding(JpaUser user, String contact, String callId, long cseq, int expires) {
        this.user = user;
        this.contact = contact;
        this.callId = callId;
        this.cSeq = cseq;
        this.expires = expires;
    }
    public String getUserName() {
        return user.getUserName();
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
        if (user != null) {
            user.setUserName(userName);
        }
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
}
