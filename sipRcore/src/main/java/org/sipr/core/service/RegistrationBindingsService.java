package org.sipr.core.service;

import java.util.List;
import java.util.Map;

public interface RegistrationBindingsService<RegistrationBinding> {
    void deleteAllBindings(String userName);

    void deleteBindings(List<RegistrationBinding> bindings);

    void saveBindings(List<RegistrationBinding> bindings);

    Map<String, RegistrationBinding> findByUserName(String userName);

    void deleteBinding(RegistrationBinding binding);

    void saveBinding(RegistrationBinding binding);

    RegistrationBinding createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires);
}
