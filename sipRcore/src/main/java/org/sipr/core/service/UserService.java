package org.sipr.core.service;

public interface UserService<User> {
    User getUser(String userName) throws UserNotFoundException;
}
