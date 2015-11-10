package org.sipr.jpa.domain;

import org.sipr.core.domain.User;

import javax.persistence.*;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames={"userName"})
        })
public class JpaUser implements User {

    @Id
    @GeneratedValue
    private Long id;

    String userName;
    String sipPassword;

    public JpaUser() {
    }

    public JpaUser(String userName) {
        this.userName = userName;
    }

    public JpaUser(String userName, String sipPassword) {
        this.userName = userName;
        this.sipPassword = sipPassword;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getSipPassword() {
        return sipPassword;
    }
}
