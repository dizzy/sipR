package org.sipr.core.domain;

public interface RegistrationBinding extends BaseBinding {
    String getUa();

    String getServer();

    void setServer(String server);
}
