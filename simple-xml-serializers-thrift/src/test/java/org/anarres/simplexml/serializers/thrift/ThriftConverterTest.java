/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.compilerworks.plugin.cdh42.common.xml;

import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.hadoop.hive.serde.test.ThriftTestObj;
import org.apache.thrift.TBase;
import org.junit.Test;
import org.simpleframework.xml.Element;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class ThriftConverterTest {

    private static final Logger LOG = LoggerFactory.getLogger(ThriftConverterTest.class);

    public static class WrapperBean {

        @Element(required = false)
        TBase<?, ?> object;

        public WrapperBean() {
        }

        public WrapperBean(TBase<?, ?> object) {
            this.object = object;
        }
    }

    private void testSerialization(ThriftTestObj in) throws Exception {
        WrapperBean w = PersisterTestUtils.testSerialization(WrapperBean.class, new WrapperBean(in));
        ThriftTestObj out = (ThriftTestObj) w.object;
        LOG.info(in + " (" + System.identityHashCode(in) + ") -> " + out + " (" + System.identityHashCode(out) + ")");
        if (in != null)
            assertNotSame(in, out);
        assertEquals(in, out);
    }

    @Test
    public void testSerialization() throws Exception {
        testSerialization(null);
        ThriftTestObj obj = new ThriftTestObj();
        testSerialization(obj);
        obj.setField1(42);
        testSerialization(obj);
        obj.setField2("aaaaaAAAAAAAAAA");
        testSerialization(obj);
    }
}
