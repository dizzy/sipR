package org.sipr.request.handler;

import org.sipr.core.domain.SubscriptionBinding;
import org.sipr.core.service.SubscriptionBindingsService;
import org.sipr.core.sip.request.handler.SubscriptionHandler;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.request.notify.NotifyContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sip.*;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
public class SubscriptionHandlerImpl implements SubscriptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionHandlerImpl.class);

    @Inject
    MessageFactory messageFactory;

    @Inject
    HeaderFactory headerFactory;

    @Inject
    SubscriptionRequestBuilder requestBuilder;

    @Inject
    SubscriptionBindingsService subscriptionBindingsService;

    @Inject
    List<NotifyContentBuilder> notifyBuilders;

    Map<String, NotifyContentBuilder> notifyBuildersMap;

    @PostConstruct
    void init() {
        notifyBuildersMap = notifyBuilders.stream().collect(toMap(NotifyContentBuilder::getEventType, (r) -> r));
    }

    @Override
    public void handleRequest(RequestEvent requestEvent) {

        try {

            SubscriptionRequest request = requestBuilder.getSubscriptionRequest(requestEvent);

            NotifyContentBuilder notifyBuilder = notifyBuildersMap.get(request.getEventType());
            if (notifyBuilder == null) {
                // create and send not implemented response
                Response response = createNotImplementedResponse(request);
                sendResponse(response, request);
                return;
            }

            // create and send subscription response
            Response response = createSubscriptionResponse(request);
            sendResponse(response, request);

            // update subscription in database
            SubscriptionBinding binding = updateSubscriptionStorage(request);

            // create NOTIFY request
            Dialog dialog = request.getDialog();
            Request notifyRequest = dialog.createRequest(Request.NOTIFY);
            addNotifyRequestHeaders(notifyRequest, request, binding);

            // delegate to build NOTIFY content
            if (!request.isUnsubscribe()) {
                notifyBuilder.addContent(notifyRequest, request);
            }

            // send NOTIFY request
            ClientTransaction ct = request.getSipProvider().getNewClientTransaction(notifyRequest);
            dialog.sendRequest(ct);

        } catch (InvalidArgumentException | ParseException | SipException ex) {
           LOGGER.error("Cannot process subscription: " + ex);
        }
    }

    void sendResponse(Response response, SubscriptionRequest request) throws InvalidArgumentException, SipException {
        ServerTransaction serverTransaction = request.getServerTransaction();
        serverTransaction.sendResponse(response);
    }

    SubscriptionBinding updateSubscriptionStorage(SubscriptionRequest request) {
        SubscriptionBinding subscription = subscriptionBindingsService.findByContactAndType(request.getContactUri(), request.getEventType());
        if (subscription == null) {
            subscription = subscriptionBindingsService.createSubscription(request.getUser(), request.getContactUri(), request.getCallId(),
                    1, request.getExpires(), request.getEventType());
        } else {
            subscription.setCallId(request.getCallId());
            subscription.setExpires(request.getExpires());
            subscription.setCseq(subscription.getCseq() + 1);
        }

        if (!request.isUnsubscribe()) {
            subscriptionBindingsService.saveSubscription(subscription);
        } else {
            subscriptionBindingsService.deleteSubscription(subscription);
        }
        return subscription;
    }

    Response createNotImplementedResponse(SubscriptionRequest request) throws ParseException {
        return messageFactory.createResponse(Response.NOT_IMPLEMENTED, request.getRequest());
    }

    Response createSubscriptionResponse(SubscriptionRequest request) throws ParseException {
        Response response = messageFactory.createResponse(request.getResponse(), request.getRequest());
        if (request.isInitialSubscribe()) {
            String toTag = Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            toHeader.setTag(toTag);
        }
        response.addHeader(request.getExpiresHeader());
        return response;
    }

    void addNotifyRequestHeaders(Request notifyRequest, SubscriptionRequest request, SubscriptionBinding binding) throws ParseException, SipException, InvalidArgumentException {
        SubscriptionStateHeader state = headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.ACTIVE);
        if (request.isUnsubscribe()) {
            state = headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.TERMINATED);
            state.setReasonCode("deactivated");
        }

        notifyRequest.setHeader(request.getEventHeader());
        notifyRequest.addHeader(state);
        notifyRequest.setHeader(headerFactory.createCSeqHeader(Long.valueOf(binding.getCseq()), Request.NOTIFY));
    }
}
