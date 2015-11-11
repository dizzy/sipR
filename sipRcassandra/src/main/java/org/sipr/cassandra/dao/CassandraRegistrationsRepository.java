package org.sipr.cassandra.dao;

import org.sipr.cassandra.domain.CassandraRegistrationBinding;
import org.sipr.core.dao.RegistrationBindingsRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CassandraRegistrationsRepository extends RegistrationBindingsRepository, CrudRepository<CassandraRegistrationBinding, Long> {

    @Override
    @Query("delete from registrations where username = ?0")
    Long deleteByUserName(@Param("userName") String userName);

    @Override
    @Query("select * from registrations where username = ?0")
    List<CassandraRegistrationBinding> findByUserName(@Param("username") String username);
}
