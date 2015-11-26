package org.sipr.jpa.service;

import org.sipr.core.domain.User;
import org.sipr.core.service.UserNotFoundException;
import org.sipr.core.service.UserService;
import org.sipr.jpa.dao.JpaUserRepository;
import org.sipr.jpa.domain.JpaUser;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserServiceImpl implements UserService {

    @Inject
    JpaUserRepository usersRepository;

    @Override
    public User getUser(String userName) throws UserNotFoundException {
        JpaUser user = (JpaUser) usersRepository.findByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
