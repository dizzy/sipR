package org.sipr.core.dao;

import java.util.List;

public interface RegistrationBindingsRepository<RegistrationBinding> {
    Long deleteByUserName(String userName);

    List<RegistrationBinding> findByUserName(String userName);

    List<RegistrationBinding> findByContact(String contact);
}
