package org.sipr.request.notify.events;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Component
public class NotifyEventFactoryImpl implements NotifyEventFactory {
    @Inject
    Jaxb2Marshaller marshaller;

    @Override
    public Object createEvent(String eventContent) {
        return marshaller.unmarshal(new StreamSource(new StringReader(eventContent)));
    }
}
