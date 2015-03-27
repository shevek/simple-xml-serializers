/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import java.io.IOException;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;
import org.simpleframework.xml.Element;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class SerializableConverterTest {

    public static class ThrowableBean {

        @Element(required = false)
        Throwable throwable;

        public ThrowableBean() {
        }

        public ThrowableBean(Throwable throwable) {
            this.throwable = throwable;
        }
    }

    private void testSerialization(ThrowableBean object, int depth) throws Exception {
        ThrowableBean copy = PersisterTestUtils.testSerialization(ThrowableBean.class, object);
        assertNotNull(copy);
        Throwable t = copy.throwable;
        int actual = 0;
        while (t != null) {
            actual++;
            t = t.getCause();
        }
        assertEquals(depth, actual);
    }

    @Test
    public void testSomeMethod() throws Exception {
        testSerialization(new ThrowableBean(null), 0);
        testSerialization(new ThrowableBean(new IOException()), 1);
        testSerialization(new ThrowableBean(new IOException(new IOException())), 2);
    }
}
