package org.sipr.core.service;

import org.sipr.core.domain.User;

public interface UserService {
    User getUser(String userName) throws UserNotFoundException;
}
