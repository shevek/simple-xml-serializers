/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.test;

import java.net.URL;
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.spring.remote.SimpleXmlInvokerProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author shevek
 */
public class RpcTestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RpcTestUtils.class);

    @Nonnull
    public static <T> T newClient(@Nonnull MockMvc mvc, @Nonnull Class<T> type, @Nonnull URL url) {
        LOG.info("Type is " + type);
        SimpleXmlInvokerProxyFactoryBean factory = new SimpleXmlInvokerProxyFactoryBean();
        factory.setBeanClassLoader(type.getClassLoader());
        // factory.setServiceUrl(RpcController.ROOT + "/" + type.getSimpleName());
        factory.setServiceUrl(url.toString());
        factory.setServiceInterface(type);
        factory.setSimpleXmlInvokerRequestExecutor(new MockMvcSimpleXmlInvokerRequestExecutor(mvc));
        LOG.info("Factory is " + factory);
        factory.afterPropertiesSet();
        T client = type.cast(factory.getObject());
        LOG.info("Client is " + client);
        return client;
    }
}
