/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class FloatArrayTransformTest {

    @Root
    public static class Wrapper {

        @Attribute(required = false)
        private float[] value;
    }

    @Test
    public void testTransform() throws Exception {
        Wrapper in = new Wrapper();
        Wrapper out = PersisterTestUtils.testSerialization(in);
        assertArrayEquals(in.value, out.value, Float.MIN_NORMAL);

        in.value = new float[]{1.1f, 2.02f, 3.003f, 4.0004f};
        out = PersisterTestUtils.testSerialization(in);
        assertArrayEquals(in.value, out.value, Float.MIN_NORMAL);
    }
}
