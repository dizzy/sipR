package org.sipr.couchdb.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sipr.core.domain.RegistrationBinding;
import org.sipr.core.service.RegistrationBindingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration
public class RegistrationBindingsServiceImplTest {

    @Autowired
    RegistrationBindingsService bindingsService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testDeleteAllBindings() throws Exception {

    }

    @Test
    public void testDeleteBindings() throws Exception {

    }

    @Test
    public void testSaveBindings() throws Exception {

    }

    @Test
    public void testFindByContact() throws Exception {

    }

    @Test
    public void testFindByUserName() throws Exception {

    }

    @Test
    public void testDeleteBinding() throws Exception {

    }

    @Test
    public void testSaveBinding() throws Exception {

    }

    @Test
    public void testCreateRegistrationBinding() throws Exception {
        //bindingsService.createRegistrationBinding("mircea", "uri", "callId", 60,2);
    }
}