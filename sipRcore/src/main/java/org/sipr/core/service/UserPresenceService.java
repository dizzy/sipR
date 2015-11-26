package org.sipr.core.service;

import org.sipr.core.domain.UserPresence;

public interface UserPresenceService {

    UserPresence getPresence(String user);

    void savePresence(UserPresence presence);

    UserPresence createPresence(String username, String presence);
}
