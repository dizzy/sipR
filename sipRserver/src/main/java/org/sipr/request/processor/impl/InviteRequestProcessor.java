package org.sipr.request.processor.impl;

import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.core.utils.SipUtils;
import org.sipr.request.processor.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.*;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Map;

@Component
public class InviteRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(InviteRequestProcessor.class);

    @Inject
    MessageFactory messageFactory;

    @Inject
    HeaderFactory headerFactory;

    @Inject
    RegistrationBindingsService registrationBindingsService;

    @Inject
    SipUtils sipUtils;

    @Inject
    AddressFactory addressFactory;

    @Override
    public void processEvent(RequestEvent requestEvent) {

        try {

            SipProvider sipProvider = (SipProvider) requestEvent.getSource();
            Request request = requestEvent.getRequest();

            ServerTransaction st = requestEvent.getServerTransaction();

            if (st == null) {
                st = sipProvider.getNewServerTransaction(request);
            }

            // send 100 TRYING
            Response trying = messageFactory.createResponse(Response.TRYING, request);
            st.sendResponse(trying);

            Response moved = messageFactory.createResponse(Response.MOVED_TEMPORARILY, request);

            String toUser = sipUtils.extractToUser(requestEvent.getRequest());

            Map<String, RegistrationBinding> bindings = registrationBindingsService.findByUserName(toUser);
            if (bindings.isEmpty()) {
                Response busy = messageFactory.createResponse(Response.BUSY_HERE, request);
                st.sendResponse(busy);
            } else {
                for (String contact : bindings.keySet()) {
                    Address address = addressFactory.createAddress(contact);
                    ContactHeader contactHeader = headerFactory.createContactHeader(address);
                    contactHeader.setExpires(10);
                    moved.addHeader(contactHeader);
                }

                ToHeader toHeader = (ToHeader) moved.getHeader(ToHeader.NAME);
                toHeader.setTag("4321");
                st.sendResponse(moved);
            }

        } catch (ParseException | SipException | InvalidArgumentException pex) {
            LOGGER.error("Exception while processing INVITE" + pex.getStackTrace());
        }

    }

    @Override
    public String getRequestType() {
        return Request.INVITE;
    }
}
