package org.sipr.core.service;

public interface UserPresenceService<UserPresence> {

    UserPresence getPresence(String user);

    void savePresence(UserPresence presence);

    UserPresence createPresence(String username, String presence);
}
