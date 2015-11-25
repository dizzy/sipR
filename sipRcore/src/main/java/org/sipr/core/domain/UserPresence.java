package org.sipr.core.domain;

public interface UserPresence {

    public final String DND = "dnd";
    public final String AVAILABLE = "available";

    String getUsername();

    String getPresence();

    void setPresence(String presence);

    boolean isDndEnabled();

}
