package org.sipr.mongodb.domain;

import org.apache.commons.lang3.StringUtils;
import org.sipr.core.domain.User;
import org.sipr.core.domain.UserPresence;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "presence")
public class MongoUserPresence implements UserPresence {
    @Id
    String id;

    @Indexed
    String userName;

    String presence;

    public MongoUserPresence(String userName, String presence) {
        this.userName = userName;
        this.presence = presence;
    }

    public String getUsername() {
        return userName;
    }

    @Override
    public void setPresence(String presence) {
        this.presence = presence;
    }

    public String getPresence() {
        return presence;
    }

    @Override
    public boolean isDndEnabled() {
        return StringUtils.equals(presence, UserPresence.DND);
    }
}
