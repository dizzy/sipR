package org.sipr.cassandra.dao;

import org.sipr.cassandra.domain.CassandraUserPresence;
import org.sipr.core.dao.UserPresenceRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CassandraUserPresenceRepository extends UserPresenceRepository, CrudRepository<CassandraUserPresence, Long> {
    @Override
    @Query("select * from user_presence where username = ?0")
    CassandraUserPresence findByUserName(@Param("username") String username);
}
