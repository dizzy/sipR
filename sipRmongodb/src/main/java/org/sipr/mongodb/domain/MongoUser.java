package org.sipr.mongodb.domain;

import org.sipr.core.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class MongoUser implements User {
    @Id
    String id;

    @Indexed
    String userName;

    String sipPassword;

    public MongoUser(String userName, String sipPassword) {
        this.userName = userName;
        this.sipPassword = sipPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getSipPassword() {
        return sipPassword;
    }
}
