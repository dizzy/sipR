package org.sipr;

import org.sipr.core.utils.SipUtils;
import org.sipr.request.processor.RequestProcessor;
import org.sipr.response.processor.ResponseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sip.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class SipRListener implements SipListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SipRListener.class);

    @Inject
    SipUtils sipUtils;

    @Inject
    List<RequestProcessor> requestProcessors;

    @Inject
    ResponseProcessor responseProcessor;

    Map<String, RequestProcessor> requestHandlersMap;

    @PostConstruct
    void init() {
        requestHandlersMap = requestProcessors.stream().collect(toMap(RequestProcessor::getRequestType, (r) -> r));
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
        String requestMethod = sipUtils.getRequestMethod(requestEvent);
        requestHandlersMap.get(upperCase(requestMethod)).processEvent(requestEvent);
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        LOGGER.info(responseEvent.getResponse().toString());
        responseProcessor.processEvent(responseEvent);
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        LOGGER.info(timeoutEvent.getTimeout().toString());
    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {
        LOGGER.info(ioExceptionEvent.toString());
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        LOGGER.info(transactionTerminatedEvent.toString());
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        LOGGER.info(dialogTerminatedEvent.toString());
    }
}
