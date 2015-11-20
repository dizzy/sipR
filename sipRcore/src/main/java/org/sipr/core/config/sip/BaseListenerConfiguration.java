package org.sipr.core.config.sip;

import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BaseListenerConfiguration {

    List<SipProvider> createSipProviders(SipStack sipStack, Set<String> addresses, int port, List<String> schemas) throws Exception {
        List<SipProvider> providers = new LinkedList<>();
        for (String address : addresses) {
            SipProvider provider = null;
            for (String schema : schemas) {
                ListeningPoint listeningPoint = sipStack.createListeningPoint(address, port, schema);
                if (provider == null) {
                    provider = sipStack.createSipProvider(listeningPoint);
                } else {
                    provider.addListeningPoint(listeningPoint);
                }
            }
            providers.add(provider);
        }
        return providers;
    }

}
