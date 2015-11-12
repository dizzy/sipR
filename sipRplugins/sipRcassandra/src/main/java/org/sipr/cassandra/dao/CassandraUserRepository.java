package org.sipr.cassandra.dao;

import org.sipr.cassandra.domain.CassandraUser;
import org.sipr.core.dao.UserRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CassandraUserRepository extends UserRepository, CrudRepository<CassandraUser, Long> {
    @Override
    @Query("select * from users where username = ?0")
    CassandraUser findByUserName(@Param("username") String username);
}
