package org.sipr.jpa.dao;

import org.sipr.core.dao.RegistrationBindingsRepository;
import org.sipr.jpa.domain.JpaRegistrationBinding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRegistrationsRepository extends RegistrationBindingsRepository, JpaRepository<JpaRegistrationBinding, String> {
}
