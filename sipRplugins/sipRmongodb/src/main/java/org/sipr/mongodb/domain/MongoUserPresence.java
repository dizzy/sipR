package org.sipr.mongodb.domain;

import org.sipr.core.domain.BaseUserPresence;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "presence")
public class MongoUserPresence extends BaseUserPresence {
    @Id
    String id;

    @Indexed
    String userName;

    public MongoUserPresence(String userName, String presence) {
        this.userName = userName;
        setPresence(presence);
    }

    public String getUsername() {
        return userName;
    }
}
