package org.sipr.core.dao;

public interface UserRepository<User> {
    User findByUserName(String userName);
}
