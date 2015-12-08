package org.sipr.core.service;

import org.sipr.core.domain.RegistrationBinding;

import java.util.List;
import java.util.Map;

public interface RegistrationBindingsService {
    void deleteAllBindings(String userName);

    void deleteBindings(List<?> bindings);

    void saveBindings(List<RegistrationBinding> bindings);

    Map<String, RegistrationBinding> findByUserName(String userName);

    void deleteBinding(RegistrationBinding binding);

    void saveBinding(RegistrationBinding binding);

    <T extends RegistrationBinding> T createRegistrationBinding(String user, String contactUri, String callId, long cseq, int expires, String ua, String server);
}
