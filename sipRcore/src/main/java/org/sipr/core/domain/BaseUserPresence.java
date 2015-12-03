package org.sipr.core.domain;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseUserPresence implements UserPresence {

    String presence;

    String fwdImmediateNumber;

    String fwdBusyNumber;

    String fwdNoAnswerNumber;

    int fwdNoAnswerRingCount;

    @Override
    public void setDnd(boolean dnd) {
        if (dnd) {
            presence = DND;
        } else {
            presence = AVAILABLE;
        }
    }

    @Override
    public boolean isDndEnabled() {
        return StringUtils.equals(presence, UserPresence.DND);
    }

    @Override
    public String getPresence() {
        return presence;
    }

    @Override
    public void setPresence(String presence) {
        this.presence = presence;
    }

    @Override
    public boolean isFwdImmediate() {
        return StringUtils.isNotBlank(fwdImmediateNumber);
    }

    @Override
    public String getFwdImmediateNumber() {
        return fwdImmediateNumber;
    }

    @Override
    public void setFwdImmediateNumber(String number) {
        fwdImmediateNumber = number;
    }

    @Override
    public boolean isFwdBusy() {
        return StringUtils.isNotBlank(fwdBusyNumber);
    }

    @Override
    public String getFwdBusyNumber() {
        return fwdBusyNumber;
    }

    @Override
    public void setFwdBusyNumber(String number) {
        fwdBusyNumber = number;
    }

    @Override
    public boolean isFwdNoAnswer() {
        return StringUtils.isNotBlank(fwdNoAnswerNumber);
    }

    @Override
    public String getFwdNoAnswerNumber() {
        return fwdNoAnswerNumber;
    }

    @Override
    public void setFwdNoAnswerNumber(String number) {
        fwdNoAnswerNumber = number;
    }

    @Override
    public int getFwdNoAnswerRingCount() {
        return fwdNoAnswerRingCount;
    }

    @Override
    public void setFwdNoAnswerRingCount(int ringCount) {
        fwdNoAnswerRingCount = ringCount;
    }

}
