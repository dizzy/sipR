package org.sipr.cassandra.domain;

import org.sipr.core.domain.BaseUserPresence;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "user_presence")
public class CassandraUserPresence extends BaseUserPresence {

    @PrimaryKey
    @Column("username")
    String userName;

    public CassandraUserPresence(String userName, String presence) {
        this.userName = userName;
        setPresence(presence);
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
