package org.sipr.jpa.dao;

import org.sipr.core.dao.RegistrationBindingsRepository;
import org.sipr.jpa.domain.JpaRegistrationBinding;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaRegistrationsRepository implements RegistrationBindingsRepository {

    @Override
    public Long deleteByUserName(String userName) {
        return null;
    }

    @Override
    public List findByUserName(String userName) {
        return null;
    }

    @Override
    public List findByContact(String contact) {
        return null;
    }

    public void delete(List<JpaRegistrationBinding> bindings) {
    }

    public void save(JpaRegistrationBinding binding) {
    }

    public void save(List<JpaRegistrationBinding> bindings) {
    }
}
