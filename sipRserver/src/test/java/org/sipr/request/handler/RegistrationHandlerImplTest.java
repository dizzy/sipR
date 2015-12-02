package org.sipr.request.handler;

import gov.nist.javax.sip.header.Expires;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.sipr.core.sip.request.processor.RequestException;
import org.sipr.utils.SipUtils;

import javax.sip.RequestEvent;
import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationHandlerImplTest {

    @Mock
    RequestEvent requestEvent;

    @Mock
    Request request;

    @Mock
    SipUtils sipUtils;

    @Mock
    ContactHeader primaryContact;

    @Mock
    ContactHeader secondaryContact;

    @Mock
    ContactHeader wildCardContact;

    @Mock
    RegistrationBindingsService registrationService;

    @Mock
    RegistrationBinding primaryBinding;

    @Mock
    RegistrationBinding secondaryBinding;

    @Mock
    RegistrationBinding newBinding;

    @Mock
    ExpiresHeader expiresHeader;

    @Mock
    Address primaryAddress;

    @Mock
    Address secondaryAddress;

    @Mock
    URI primaryUri;

    @Mock
    URI secondaryUri;

    @Mock
    CallIdHeader callIdHeader;

    @Mock
    CSeqHeader cSeqHeader;

    List contactHeaders = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        when(requestEvent.getRequest()).thenReturn(request);
        when(request.getHeader(ExpiresHeader.NAME)).thenReturn(expiresHeader);
        when(sipUtils.extractAuthUser(request)).thenReturn("200");
        when(callIdHeader.getCallId()).thenReturn("qrscOPIIpEfXcqpOmHt6UdWn2Q6XJUiR");
        when(request.getHeader(CallIdHeader.NAME)).thenReturn(callIdHeader);
        when(cSeqHeader.getSeqNumber()).thenReturn(5L);
        when(request.getHeader(CSeqHeader.NAME)).thenReturn(cSeqHeader);

        contactHeaders.add(primaryContact);
        contactHeaders.add(secondaryContact);

        Map<String, RegistrationBinding> bindings = new HashMap<>();
        bindings.put("sip:200@192.168.0.60:59177;ob", primaryBinding);
        bindings.put("sip:200@192.168.0.81:5999;ob", secondaryBinding);
        when(registrationService.findByUserName("200")).thenReturn(bindings);
    }

    @Test
    public void testBadRequestWildCardHeaders() throws Exception {
        when(sipUtils.containsWildCardHeader(contactHeaders)).thenReturn(true);
        when(sipUtils.extractContactHeaders(request)).thenReturn(contactHeaders);
        testException();

        List wildCardHeaders = Collections.singletonList(wildCardContact);
        when(sipUtils.extractContactHeaders(request)).thenReturn(wildCardHeaders);
        when(sipUtils.containsWildCardHeader(wildCardHeaders)).thenReturn(true);
        when(request.getHeader(ExpiresHeader.NAME)).thenReturn(null);
        testException();

        when(sipUtils.extractContactHeaders(request)).thenReturn(wildCardHeaders);
        when(sipUtils.containsWildCardHeader(wildCardHeaders)).thenReturn(true);
        when(expiresHeader.getExpires()).thenReturn(6);
        when(request.getHeader(ExpiresHeader.NAME)).thenReturn(expiresHeader);
        testException();
    }

    private void testException() {
        RegistrationHandlerImpl registrationHandler = new RegistrationHandlerImpl();
        registrationHandler.registrationService = registrationService;
        registrationHandler.sipUtils = sipUtils;
        try {
            registrationHandler.handleRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertEquals(ex.getErrorCode(), new Integer(400));
        }
    }

    @Test
    public void testWildCardHeader() throws Exception {
        List headers = Collections.singletonList(primaryContact);
        when(sipUtils.extractContactHeaders(request)).thenReturn(headers);
        when(sipUtils.containsWildCardHeader(headers)).thenReturn(true);

        RegistrationHandlerImpl registrationHandler = new RegistrationHandlerImpl();
        registrationHandler.registrationService = registrationService;
        registrationHandler.sipUtils = sipUtils;
        Collection<RegistrationBinding> bindings = registrationHandler.handleRequest(requestEvent);

        verify(registrationService).deleteAllBindings("200");
        assertEquals(0, bindings.size());
    }

    @Test
    public void testHandleRequest() throws Exception {
        when(primaryAddress.getURI()).thenReturn(primaryUri);
        when(primaryUri.toString()).thenReturn("sip:200@192.168.0.60:59177;ob");
        when(primaryContact.getAddress()).thenReturn(primaryAddress);

        when(secondaryAddress.getURI()).thenReturn(secondaryUri);
        when(secondaryUri.toString()).thenReturn("sip:200@192.168.0.77:59177;ob");
        when(secondaryContact.getAddress()).thenReturn(secondaryAddress);
        when(secondaryContact.getExpires()).thenReturn(-1);

        when(sipUtils.extractContactHeaders(request)).thenReturn(contactHeaders);
        when(sipUtils.containsWildCardHeader(contactHeaders)).thenReturn(false);

        when(registrationService.createRegistrationBinding("200",
                "sip:200@192.168.0.77:59177;ob", "qrscOPIIpEfXcqpOmHt6UdWn2Q6XJUiR", 5, 50)).thenReturn(newBinding);
        when(newBinding.getContact()).thenReturn("sip:200@192.168.0.77:59177;ob");

        RegistrationHandlerImpl registrationHandler = new RegistrationHandlerImpl();
        registrationHandler.registrationService = registrationService;
        registrationHandler.sipUtils = sipUtils;
        registrationHandler.serverExpire = 50;
        Collection<RegistrationBinding> bindings = registrationHandler.handleRequest(requestEvent);

        verify(registrationService).deleteBindings(Collections.singletonList(primaryBinding));
        verify(registrationService).saveBindings(Collections.singletonList(newBinding));
        assertEquals(3, bindings.size());

        when(primaryBinding.getCseq()).thenReturn(7L);
        when(primaryBinding.getExpires()).thenReturn(-1);
        when(primaryBinding.getCallId()).thenReturn("qrscOPIIpEfXcqpOmHt6UdWn2Q6XJUiR");
        try {
            registrationHandler.handleRequest(requestEvent);
            fail();
        } catch (RequestException ex) {
            assertEquals(ex.getErrorCode(), new Integer(400));
        }

        when(primaryBinding.getCseq()).thenReturn(4L);
        when(primaryContact.getExpires()).thenReturn(0);
        bindings = registrationHandler.handleRequest(requestEvent);
        assertEquals(2, bindings.size());

        when(primaryContact.getExpires()).thenReturn(-1);
        when(expiresHeader.getExpires()).thenReturn(6);
        bindings = registrationHandler.handleRequest(requestEvent);

        verify(primaryBinding).setCseq(5L);
        verify(primaryBinding).setExpires(50);
        assertEquals(3, bindings.size());

        when(primaryContact.getExpires()).thenReturn(70);
        registrationHandler.handleRequest(requestEvent);
        verify(primaryBinding).setExpires(70);
    }

}