package org.sipr.core.dao;

public interface UserPresenceRepository<UserPresence> {
    UserPresence findByUserName(String userName);
}
