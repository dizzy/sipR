package org.sipr.registrar.request;

import org.sipr.core.utils.SipUtils;
import org.sipr.registrar.request.handler.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import javax.sip.*;

import static java.lang.String.format;

public class RequestDispatcher implements SipListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestDispatcher.class);
    private static final String PROCESSOR = "%sHandler";

    @Autowired
    ApplicationContext applicationContext;

    @Inject
    SipUtils sipUtils;

    @Override
    public void processRequest(RequestEvent requestEvent) {
        String callId = sipUtils.getCallId(requestEvent);
        String requestMethod = sipUtils.getRequestMethod(requestEvent);
        LOGGER.debug(format("Process Request Call Id=%s, method=%s", callId, requestMethod));
        RequestHandler handler = (RequestHandler) applicationContext.getBean(format(PROCESSOR, requestMethod.toLowerCase()));
        handler.handle(requestEvent);
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        LOGGER.info(responseEvent.getResponse().toString());
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
