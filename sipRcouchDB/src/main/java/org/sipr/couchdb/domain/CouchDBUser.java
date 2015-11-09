package org.sipr.couchdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;
import org.sipr.core.domain.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouchDBUser extends CouchDbDocument implements User {

    String userName;

    @TypeDiscriminator
    String sipPassword;

    public CouchDBUser() {

    }

    public CouchDBUser(String userName, String sipPassword) {
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

    public void setSipPassword(String sipPassword) {
        this.sipPassword = sipPassword;
    }
}
