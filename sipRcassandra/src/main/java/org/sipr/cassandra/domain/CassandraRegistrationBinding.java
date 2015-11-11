package org.sipr.cassandra.domain;

import org.sipr.core.domain.RegistrationBinding;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "registrations")
public class CassandraRegistrationBinding implements RegistrationBinding {

    @PrimaryKeyColumn(name = "username", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    String userName;

    @PrimaryKeyColumn(name = "contact", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    String contact;

    @Column("call_id")
    String callId;

    @Column("cseq")
    long cseq;

    @Column("expires")
    int expires;

    public CassandraRegistrationBinding(String userName, String contact, String callId, long cseq, int expires) {
        this.userName = userName;
        this.contact = contact;
        this.callId = callId;
        this.cseq = cseq;
        this.expires = expires;
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
    }

}
