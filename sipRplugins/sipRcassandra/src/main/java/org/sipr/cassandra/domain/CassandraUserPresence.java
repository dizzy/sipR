package org.sipr.cassandra.domain;

import org.apache.commons.lang3.StringUtils;
import org.sipr.core.domain.UserPresence;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value = "user_presence")
public class CassandraUserPresence implements UserPresence {

    @PrimaryKey
    @Column("username")
    String userName;

    @Column("presence")
    String presence;

    public CassandraUserPresence(String userName, String presence) {
        this.userName = userName;
        this.presence = presence;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPresence() {
        return null;
    }

    @Override
    public void setPresence(String presence) {
        this.presence = presence;
    }

    @Override
    public boolean isDndEnabled() {
        return StringUtils.equals(presence, UserPresence.DND);
    }
}
