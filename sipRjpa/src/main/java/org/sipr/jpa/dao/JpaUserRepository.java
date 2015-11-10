package org.sipr.jpa.dao;

import org.sipr.core.dao.UserRepository;
import org.sipr.jpa.domain.JpaUser;
import org.springframework.data.repository.CrudRepository;

public interface JpaUserRepository extends UserRepository, CrudRepository<JpaUser, Long> {
}