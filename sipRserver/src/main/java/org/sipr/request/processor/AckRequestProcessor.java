package org.sipr.request.processor;

import org.sipr.utils.SipMessageSender;
import org.sipr.core.sip.request.processor.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sip.*;
import javax.sip.message.Request;
import javax.sip.message.Response;

@Component
public class AckRequestProcessor implements RequestProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AckRequestProcessor.class);

    @Inject
    SipMessageSender sipMessageSender;

    @Override
    public void processEvent(RequestEvent requestEvent) {
        try {
            SipProvider sipProvider = (SipProvider) requestEvent.getSource();
            ServerTransaction st = requestEvent.getServerTransaction();
            Dialog dialog = st.getDialog();
            if (dialog.getState() == DialogState.CONFIRMED) {
                Request byeRequest = dialog.createRequest(Request.BYE);
                ClientTransaction tr = sipProvider.getNewClientTransaction(byeRequest);
                dialog.sendRequest(tr);
            }
        } catch (SipException ex) {
            LOGGER.error("Exception while processing ACK " + ex.getStackTrace());
            sipMessageSender.sendResponse(requestEvent, Response.SERVER_INTERNAL_ERROR);
        }

    }

    @Override
    public String getRequestType() {
        return Request.ACK;
    }
}
