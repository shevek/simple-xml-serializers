/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.spring.remote.SimpleXmlInvocationResult;
import org.anarres.simplexml.serializers.spring.remote.SimpleXmlInvokerClientConfiguration;
import org.anarres.simplexml.serializers.spring.remote.SimpleXmlInvokerRequestExecutor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author shevek
 */
public class MockMvcSimpleXmlInvokerRequestExecutor extends SimpleXmlInvokerRequestExecutor {

    private final MockMvc mvc;

    public MockMvcSimpleXmlInvokerRequestExecutor(@Nonnull MockMvc mvc) {
        this.mvc = mvc;
    }

    @Override
    protected SimpleXmlInvocationResult doExecuteRequest(SimpleXmlInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
        MockHttpServletRequestBuilder builder = post(config.getServiceUrl());
        builder.content(baos.toByteArray());
        ResultActions result = mvc.perform(builder);
        result.andExpect(status().isOk());
        byte[] responseBody = result.andReturn().getResponse().getContentAsByteArray();
        return readSimpleXmlInvocationResult(config, new ByteArrayInputStream(responseBody));
    }
}
