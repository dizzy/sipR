package org.sipr.mongodb.domain;

import org.sipr.core.domain.SubscriptionBinding;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Document(collection = "subscriptions")
public class MongoSubscriptionBinding implements SubscriptionBinding {
    @Id
    String id;

    @Indexed
    String userName;

    @Indexed
    String contact;

    @Indexed
    String type;

    String callId;
    long cseq;
    int expires;

    @Indexed(name = "expireAt", expireAfterSeconds = 0)
    Date expireAt;

    public MongoSubscriptionBinding(String userName, String contact, String callId, long cseq, int expires, String type) {
        this.userName = userName;
        this.contact = contact;
        this.callId = callId;
        this.cseq = cseq;
        this.type = type;
        setExpires(expires);
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

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
