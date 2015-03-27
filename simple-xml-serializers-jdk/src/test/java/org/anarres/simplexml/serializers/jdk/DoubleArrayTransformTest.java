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
public class DoubleArrayTransformTest {

    @Root
    public static class Wrapper {

        @Attribute(required = false)
        private double[] value;
    }

    @Test
    public void testTransform() throws Exception {
        Wrapper in = new Wrapper();
        Wrapper out = PersisterTestUtils.testSerialization(in);
        assertArrayEquals(in.value, out.value, Double.MIN_NORMAL);

        in.value = new double[]{1.1d, 2.02d, 3.003d, 4.0004d};
        out = PersisterTestUtils.testSerialization(in);
        assertArrayEquals(in.value, out.value, Double.MIN_NORMAL);
    }
}
