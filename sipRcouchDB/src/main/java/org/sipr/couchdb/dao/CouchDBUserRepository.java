package org.sipr.couchdb.dao;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.sipr.core.dao.UserRepository;
import org.sipr.couchdb.domain.CouchDBUser;
import org.springframework.stereotype.Component;

import java.util.List;

public class CouchDBUserRepository extends CouchDbRepositorySupport<CouchDBUser> implements UserRepository<CouchDBUser> {

    public CouchDBUserRepository(CouchDbConnector db) {
        super(CouchDBUser.class, db);
        initStandardDesignDocument();
    }

    @Override
    public CouchDBUser findByUserName(String userName) {
        List<CouchDBUser> users = findByUserNameList(userName);
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @GenerateView(field = "userName")
    private List<CouchDBUser> findByUserNameList(String userName) {
        List<CouchDBUser> users = queryView("by_userNameList", userName);
        return users;
    }
}
