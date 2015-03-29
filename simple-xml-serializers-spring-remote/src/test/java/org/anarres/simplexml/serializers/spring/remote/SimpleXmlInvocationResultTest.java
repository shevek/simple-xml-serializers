/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;

/**
 *
 * @author shevek
 */
public class SimpleXmlInvocationResultTest {

    @Test
    public void testSerializationEmpty() throws Exception {
        SimpleXmlInvocationResult result = new SimpleXmlInvocationResult();
        result.setValue(null);
        PersisterTestUtils.testSerialization(SimpleXmlInvocationResult.class, result);
    }

    @Test
    public void testSerializationSimple() throws Exception {
        SimpleXmlInvocationResult result = new SimpleXmlInvocationResult();
        result.setValue("foo");
        PersisterTestUtils.testSerialization(SimpleXmlInvocationResult.class, result);
    }

    @Test
    public void testSerializationwTypeist() throws Exception {
        SimpleXmlInvocationResult result = new SimpleXmlInvocationResult();
        result.setValue(Map.class);
        PersisterTestUtils.testSerialization(SimpleXmlInvocationResult.class, result);
    }

    @Test
    public void testSerializationList() throws Exception {
        SimpleXmlInvocationResult result = new SimpleXmlInvocationResult();
        List<String> list = new ArrayList<String>();
        list.add("foo");
        result.setValue(list);
        PersisterTestUtils.testSerialization(SimpleXmlInvocationResult.class, result);
    }
}
