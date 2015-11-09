package org.sipr.couchdb.service;

import org.sipr.core.service.UserNotFoundException;
import org.sipr.core.service.UserService;
import org.sipr.couchdb.dao.CouchDBUserRepository;
import org.sipr.couchdb.domain.CouchDBUser;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserServiceImpl implements UserService {

    @Inject
    public CouchDBUserRepository usersRepository;

    @Override
    public CouchDBUser getUser(String userName) throws UserNotFoundException {
        CouchDBUser user = (CouchDBUser) usersRepository.findByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
