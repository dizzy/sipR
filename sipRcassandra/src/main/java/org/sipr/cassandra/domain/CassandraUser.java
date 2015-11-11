package org.sipr.cassandra.domain;

import org.sipr.core.domain.User;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "users")
public class CassandraUser implements User {

    @PrimaryKey
    @Column("username")
    String userName;

    @Column("sip_pwd")
    String sipPassword;

    public CassandraUser(String userName, String sipPassword) {
        this.userName = userName;
        this.sipPassword = sipPassword;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getSipPassword() {
        return sipPassword;
    }
}
