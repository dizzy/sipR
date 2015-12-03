package org.sipr.core.domain;

public interface UserPresence {

    String DND = "dnd";
    String AVAILABLE = "available";
    String FORWARD_IMMEDIATE = "forwardImmediate";
    String FORWARD_BUSY = "forwardBusy";
    String FORWARD_NO_ANSWER = "forwardNoAns";

    String getUsername();

    String getPresence();

    void setPresence(String presence);

    void setDnd(boolean dnd);

    boolean isDndEnabled();

    boolean isFwdImmediate();

    String getFwdImmediateNumber();

    void setFwdImmediateNumber(String number);

    boolean isFwdBusy();

    String getFwdBusyNumber();

    void setFwdBusyNumber(String number);

    boolean isFwdNoAnswer();

    String getFwdNoAnswerNumber();

    void setFwdNoAnswerNumber(String number);

    int getFwdNoAnswerRingCount();

    void setFwdNoAnswerRingCount(int ringCount);

}
