/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring;

import java.net.InetAddress;
import java.net.URI;
import org.anarres.simplexml.factory.PersisterFactory;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author shevek
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SimpleXmlSerializersConfiguration.class)
// @ActiveProfiles({})
// @DirtiesContext
// @TestPropertySource()
public class SimpleXmlSerializersConfigurationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleXmlSerializersConfigurationTest.class);

    @Autowired
    private PersisterFactory factory;

    private static class Bean {

        @Attribute
        InetAddress aAddress;
        @Element
        InetAddress eAddress;
        @Attribute
        URI aUri;
        @Element
        URI eUri;
    }

    @Test
    public void testPersister() throws Exception {
        Persister persister = factory.newPersister();
        LOG.info("Serializer is " + persister);
        PersisterTestUtils.testSerialization(persister, new int[]{1, 2, 3, 4, 5});
        PersisterTestUtils.testSerialization(persister, InetAddress.getByAddress(new byte[]{1, 2, 3, 4}));

        Bean bean = new Bean();
        bean.aAddress = InetAddress.getByAddress(new byte[]{3, 4, 5, 6});
        bean.eAddress = InetAddress.getByAddress(new byte[]{3, 4, 5, 7});
        bean.aUri = URI.create("mailto:nobody@localhost");
        bean.eUri = URI.create("mailto:somebody@localhost");
        PersisterTestUtils.testSerialization(persister, bean);
    }

}
