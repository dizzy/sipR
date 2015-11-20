package org.sipr.collector;

import org.sipr.core.sip.request.processor.RequestProcessor;
import org.sipr.utils.SipUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sip.*;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class SipRListener implements SipListener {

    @Inject
    SipUtils sipUtils;

    @Inject
    List<RequestProcessor> requestProcessors;

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
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
    }
}
