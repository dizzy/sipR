package org.sipr.cassandra.service;

import org.sipr.cassandra.dao.CassandraUserRepository;
import org.sipr.cassandra.domain.CassandraUser;
import org.sipr.core.service.UserNotFoundException;
import org.sipr.core.service.UserService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserServiceImpl implements UserService<CassandraUser> {

    @Inject
    CassandraUserRepository usersRepository;

    @Override
    public CassandraUser getUser(String userName) throws UserNotFoundException {
        CassandraUser user = (CassandraUser) usersRepository.findByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
