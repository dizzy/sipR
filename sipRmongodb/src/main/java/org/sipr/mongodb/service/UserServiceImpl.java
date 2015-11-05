package org.sipr.mongodb.service;

import org.sipr.core.service.UserNotFoundException;
import org.sipr.core.service.UserService;
import org.sipr.mongodb.dao.MongoUserRepository;
import org.sipr.mongodb.domain.MongoUser;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserServiceImpl implements UserService<MongoUser> {

    @Inject
    MongoUserRepository usersRepository;

    @Override
    public MongoUser getUser(String userName) throws UserNotFoundException {
        MongoUser user = (MongoUser) usersRepository.findByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
