package org.sipr.core.service;

import org.sipr.core.domain.AuthDetails;

public interface AuthenticationService {
    String getRealm();

    String generateNonce();

    boolean authenticate(AuthDetails authDetails, String user);

    String getAlgorithm();
}
