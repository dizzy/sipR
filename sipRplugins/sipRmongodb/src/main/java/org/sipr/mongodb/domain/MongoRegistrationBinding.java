package org.sipr.mongodb.domain;

import org.sipr.core.domain.RegistrationBinding;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Document(collection = "registrations")
public class MongoRegistrationBinding implements RegistrationBinding {
    @Id
    String id;

    @Indexed
    String userName;

    @Indexed
    String contact;
    String callId;
    long cseq;
    int expires;

    @Indexed(name = "expireAt", expireAfterSeconds = 0)
    Date expireAt;

    String ua;

    String server;

    public MongoRegistrationBinding(String userName, String contact, String callId, long cseq, int expires, String ua, String server) {
        this.userName = userName;
        this.contact = contact;
        this.callId = callId;
        this.cseq = cseq;
        setExpires(expires);
        this.ua = ua;
        this.server = server;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public long getCseq() {
        return cseq;
    }

    public void setCseq(long cseq) {
        this.cseq = cseq;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
        expireAt = Date.from(ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(5 + expires).toInstant());
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

}
