package org.sipr.request.notify;

import org.sipr.core.domain.SubscriptionBinding;
import org.sipr.core.service.SubscriptionBindingsService;
import org.sipr.request.handler.SubscriptionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.*;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Component
public class NotifySenderImpl implements NotifySender {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifySenderImpl.class);

    @Inject
    SubscriptionBindingsService subscriptionService;

    @Inject
    MessageFactory messageFactory;

    @Inject
    AddressFactory addressFactory;

    @Inject
    HeaderFactory headerFactory;

    @Override
    @Async
    public void sendNotifyToAllSubscribers(SubscriptionRequest request, ContentTypeHeader contentTypeHeader, String content) {
        try {
            // create common headers for NOTIFY message
            ListeningPoint listeningPoint = getListeningPoint(request);
            FromHeader fromHeader = createFromHeader(listeningPoint);
            ViaHeader viaHeader = createViaHeader(listeningPoint);
            CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(new Long(1), Request.NOTIFY);
            MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
            SubscriptionStateHeader stateHeader = headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.ACTIVE);

            // get subscriptions and send NOTIFY messages
            List<SubscriptionBinding> bindings = subscriptionService.findByUserNameAndType(request.getUser(), request.getEventType());
            for (SubscriptionBinding binding : bindings) {
                if (!binding.getContact().equals(request.getContactUri())) {
                    sendNotifyToSubscription(request, binding, cSeqHeader, viaHeader, fromHeader, maxForwardsHeader,
                            contentTypeHeader, stateHeader, content);
                }
            }
        } catch (ParseException | InvalidArgumentException pex) {
            LOGGER.error("Failed to notify subscription: " + pex.getMessage());
        }
    }

    ListeningPoint getListeningPoint(SubscriptionRequest request) {
        SipProvider sipProvider = request.getSipProvider();
        ListeningPoint[] lps = sipProvider.getListeningPoints();
        return lps[0];
    }

    FromHeader createFromHeader(ListeningPoint listeningPoint) throws ParseException {
        SipURI sipURI = addressFactory.createSipURI(null, listeningPoint.getIPAddress());
        sipURI.setPort(listeningPoint.getPort());
        sipURI.setLrParam();
        Address serverAddress = addressFactory.createAddress(sipURI);
        return headerFactory.createFromHeader(serverAddress, "sipR");
    }

    ViaHeader createViaHeader(ListeningPoint listeningPoint) throws ParseException, InvalidArgumentException {
        return headerFactory.createViaHeader(listeningPoint.getIPAddress(), listeningPoint.getPort(),
                listeningPoint.getTransport(), "sipr");
    }

    void sendNotifyToSubscription(SubscriptionRequest request, SubscriptionBinding binding, CSeqHeader cSeqHeader, ViaHeader viaHeader,
                                  FromHeader fromHeader, MaxForwardsHeader maxForwardsHeader, ContentTypeHeader contentTypeHeader,
                                  SubscriptionStateHeader stateHeader, String content) {
        try {
            Address address = addressFactory.createAddress(binding.getContact());
            CallIdHeader callIdHeader = headerFactory.createCallIdHeader(binding.getCallId());
            ToHeader toHeader = headerFactory.createToHeader(address, request.getUser());
            Request notify = messageFactory.createRequest(address.getURI(), Request.NOTIFY, callIdHeader, cSeqHeader, fromHeader, toHeader,
                    Collections.singletonList(viaHeader), maxForwardsHeader, contentTypeHeader, content);
            notify.addHeader(stateHeader);
            notify.addHeader(request.getEventHeader());

            ClientTransaction ctx = request.getSipProvider().getNewClientTransaction(notify);
            ctx.sendRequest();
        } catch (ParseException | SipException pex) {
            LOGGER.error(String.format("Failed to send notify type:%s user:%s contact:%s content:%s triggered by:%s reason:%s",
                    binding.getType(), binding.getUserName(), binding.getContact(), content, request.getContactUri(),
                    pex.getMessage()));
        }

    }

}
