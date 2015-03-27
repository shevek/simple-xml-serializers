/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import org.simpleframework.xml.ElementArray;
import java.io.IOException;
import java.util.Arrays;
import org.anarres.simplexml.serializers.common.SuppressWarnings;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class JavaPersistenceTest {

    private static final Logger LOG = LoggerFactory.getLogger(JavaPersistenceTest.class);

    // @Attribute fails. @Element fails.
    @Root
    public static class IntArray {

        private int[] values;

        @SuppressWarnings("EI_EXPOSE_REP2")
        public IntArray(@ElementArray(name = "values") int[] values) {
            this.values = values;
        }

        @ElementArray(name = "values")
        @SuppressWarnings("EI_EXPOSE_REP")
        public int[] getBody() {
            return values;
        }
    }

    private void testIntArray(int... values) throws IOException {
        IntArray out = PersisterTestUtils.testSerialization(new IntArray(values));
        LOG.info("Expecting " + Arrays.toString(values));
        LOG.info("Got " + Arrays.toString(out.values));
        assertArrayEquals(values, out.values);
    }

    @Test
    public void testNewDeserializer() throws Exception {
        testIntArray();
        testIntArray(0);
        testIntArray(0, 1, 2, 3);
    }
}
