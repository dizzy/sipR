package org.sipr.registrar.request.handler;

import akka.actor.UntypedActor;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.GenericURI;
import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.SipMessageSender;
import org.sipr.registrar.request.processor.RequestProcessor;
import org.sipr.registrar.request.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.header.ContactHeader;
import javax.sip.header.DateHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

@Component("registerHandler")
@Scope("prototype")
public class RegisterRequestHandler extends UntypedActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterRequestHandler.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Inject
    List<RequestValidator> requestValidators;

    @Inject
    RequestProcessor requestProcessor;

    @Inject
    HeaderFactory headerFactory;

    @Override
    public void onReceive(Object message) throws Exception {
        LOGGER.debug(format("Enter actor register processing %s", self().path().name()));

        if (message instanceof RequestEvent) {
            processRequestEvent((RequestEvent) message);
        }

        LOGGER.debug(format("Exit actor register processing %s", self().path().name()));
        getContext().stop(self());
    }

    void processRequestEvent(RequestEvent requestEvent) {
        try {

            // run request through valiator chain
            for (RequestValidator requestValidator : requestValidators) {
                requestValidator.validateRequest(requestEvent);
            }

            Collection<RegistrationBinding> bindings = requestProcessor.processRequest(requestEvent);
            sipMessageSender.sendResponse(requestEvent, Response.OK, createResponseHeaders(bindings));

        } catch (RequestException ex) {
            sipMessageSender.sendResponse(requestEvent, ex.getErrorCode(), ex.getHeaders());
        } catch (ParseException | InvalidArgumentException pex) {
            sipMessageSender.sendResponse(requestEvent, Response.SERVER_INTERNAL_ERROR);
        }
    }

    List<Header> createResponseHeaders(Collection<RegistrationBinding> bindings) throws InvalidArgumentException, ParseException {
        List<Header> headers = new ArrayList<>();

        // Add contacts from bindings to response
        for (RegistrationBinding binding : bindings) {
            ContactHeader contactHeader = headerFactory.createContactHeader();
            contactHeader.setExpires(binding.getExpires());
            Address address = new AddressImpl();
            address.setURI(new GenericURI(binding.getContact()));
            contactHeader.setAddress(address);
            headers.add(contactHeader);
        }


        Calendar c = Calendar.getInstance();
        DateHeader dateHeader = headerFactory.createDateHeader(c);
        headers.add(dateHeader);
        return headers;
    }
}
