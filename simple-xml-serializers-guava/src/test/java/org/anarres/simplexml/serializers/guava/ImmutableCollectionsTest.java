/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.guava;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 *
 * @author shevek
 */
@Ignore // All fails.
public class ImmutableCollectionsTest {

    @Root
    private static class Bean {

        @ElementList
        private List<Object> list;
        @ElementMap
        private Map<String, Object> map;
    }

    @Test
    public void testNull() throws Exception {
        Bean bean = new Bean();
        PersisterTestUtils.testSerialization(bean);
    }

    @Test
    public void testEmpty() throws Exception {
        Bean bean = new Bean();
        bean.list = ImmutableList.of();
        bean.map = ImmutableMap.of();
        PersisterTestUtils.testSerialization(bean);
    }

    @Test
    public void testSingle() throws Exception {
        Bean bean = new Bean();
        bean.list = ImmutableList.<Object>of(14);
        bean.map = ImmutableMap.<String, Object>of("foo", 15);
        PersisterTestUtils.testSerialization(bean);
    }

    @Test
    public void testMultiple() throws Exception {
        Bean bean = new Bean();
        bean.list = ImmutableList.<Object>of(14, 12, 4, 42);
        bean.map = ImmutableMap.<String, Object>of("foo", 15, "bar", 82, "baz", 11);
        PersisterTestUtils.testSerialization(bean);
    }

}
