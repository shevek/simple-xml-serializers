/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;
import static org.anarres.simplexml.serializers.spring.remote.SimpleXmlInvocation.EMPTY_CLASS_ARRAY;
import static org.anarres.simplexml.serializers.spring.remote.SimpleXmlInvocation.EMPTY_OBJECT_ARRAY;

/**
 *
 * @author shevek
 */
public class SimpleXmlInvocationTest {

    @Test
    public void testSerializationEmpty() throws IOException {
        SimpleXmlInvocation in = new SimpleXmlInvocation("foo", new Class<?>[]{}, new Object[]{});
        PersisterTestUtils.testSerialization(SimpleXmlInvocation.class, in);
    }

    @Test
    public void testSerializationFull() throws IOException {
        SimpleXmlInvocation in = new SimpleXmlInvocation("foo", new Class<?>[]{Integer.class}, new Object[]{42});
        PersisterTestUtils.testSerialization(SimpleXmlInvocation.class, in);
    }

    @Test
    public void testSerializationList() throws IOException {
        List<Object> list = new ArrayList<Object>();
        list.add("xxx");
        list.add(String.class);
        // list.add(new ArrayList<String>());
        SimpleXmlInvocation in = new SimpleXmlInvocation("foo", new Class<?>[]{List.class}, new Object[]{list});
        PersisterTestUtils.testSerialization(SimpleXmlInvocation.class, in);
    }

    @Test
    public void testSerializationAttrsOpt() throws IOException {
        SimpleXmlInvocation in = new SimpleXmlInvocation("foo", EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
        in.setAttributes(Collections.<String, Serializable>emptyMap());
        PersisterTestUtils.testSerialization(SimpleXmlInvocation.class, in);
    }

    @Test
    public void testSerializationAttrsEmpty() throws IOException {
        SimpleXmlInvocation in = new SimpleXmlInvocation("foo", EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
        in.setAttributes(new HashMap<String, Serializable>());
        PersisterTestUtils.testSerialization(SimpleXmlInvocation.class, in);
    }

    @Test
    public void testSerializationAttrsFull() throws IOException {
        SimpleXmlInvocation in = new SimpleXmlInvocation("foo", EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
        Map<String, Serializable> attrs = new HashMap<String, Serializable>();
        attrs.put("foo", "bar");
        attrs.put("baz", 42);
        in.setAttributes(attrs);
        PersisterTestUtils.testSerialization(SimpleXmlInvocation.class, in);
    }
}
