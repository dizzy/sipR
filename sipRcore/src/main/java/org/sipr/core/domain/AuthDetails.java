package org.sipr.core.domain;

public interface AuthDetails {

    String getAlgorithm();

    String getUsername();

    String getRealm();

    String getUri();

    String getNonce();

    String getCnonce();

    String getResponse();

    String getMethod();
}
