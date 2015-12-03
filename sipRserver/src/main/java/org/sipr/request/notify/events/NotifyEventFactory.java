package org.sipr.request.notify.events;

public interface NotifyEventFactory {
    Object createEvent(String eventContent);
}
