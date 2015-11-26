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
    public void handleRequest(RequestEvent requestEvent) throws RequestException {

        try {

            SubscriptionRequest request = requestBuilder.getSubscriptionRequest(requestEvent);

            NotifyContentBuilder notifyBuilder = getNotifier(request);

            // update subscription in database
            updateSubscriptionStorage(request);

            // create and send subscription response
            Response response = createSubscriptionResponse(request);
            ServerTransaction serverTransaction = request.getServerTransaction();
            serverTransaction.sendResponse(response);

            // create NOTIFY request
            Dialog dialog = request.getDialog();
            Request notifyRequest = dialog.createRequest(Request.NOTIFY);
            addNotifyRequestHeaders(notifyRequest, request);

            // delegate to build NOTIFY content
            if (!request.isUnsubscribe()) {
                notifyBuilder.addContent(notifyRequest, request);
            }

            // send NOTIFY request
            ClientTransaction ct = request.getSipProvider().getNewClientTransaction(notifyRequest);
            dialog.sendRequest(ct);

        } catch (InvalidArgumentException | ParseException | SipException ex) {
            throw new RequestException(Response.SERVER_INTERNAL_ERROR);
        }
    }

    NotifyContentBuilder getNotifier(SubscriptionRequest request) throws RequestException {
        NotifyContentBuilder notifyBuilder = notifyBuildersMap.get(request.getEventType());
        if (notifyBuilder == null) {
            throw new RequestException(Response.NOT_IMPLEMENTED);
        }
        return notifyBuilder;
    }

    void updateSubscriptionStorage(SubscriptionRequest request) {
        SubscriptionBinding subscription = subscriptionBindingsService.findByContactAndType(request.getContactUri(), request.getEventType());
        if (subscription == null) {
            subscription = subscriptionBindingsService.createSubscription(request.getUser(), request.getContactUri(), request.getCallId(),
                    request.getCSeq(), request.getExpires(), request.getEventType());
        } else {
            subscription.setCallId(request.getCallId());
            subscription.setCseq(request.getCSeq());
            subscription.setExpires(request.getExpires());
        }

        if (!request.isUnsubscribe()) {
            subscriptionBindingsService.saveSubscription(subscription);
        } else {
            subscriptionBindingsService.deleteSubscription(subscription);
        }
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

    void addNotifyRequestHeaders(Request notifyRequest, SubscriptionRequest request) throws ParseException, SipException {
        SubscriptionStateHeader state = headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.ACTIVE);
        if (request.isUnsubscribe()) {
            state = headerFactory.createSubscriptionStateHeader(SubscriptionStateHeader.TERMINATED);
            state.setReasonCode("deactivated");
        }

        notifyRequest.setHeader(request.getEventHeader());
        notifyRequest.addHeader(state);
    }
}
