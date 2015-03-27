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
public class ShortArrayTransformTest {

    @Root
    public static class Wrapper {

        @Attribute(required = false)
        private short[] value;
    }

    @Test
    public void testTransform() throws Exception {
        Wrapper in = new Wrapper();
        Wrapper out = PersisterTestUtils.testSerialization(in);
        assertArrayEquals(in.value, out.value);

        in.value = new short[]{1, 2, 3, 4};
        out = PersisterTestUtils.testSerialization(in);
        assertArrayEquals(in.value, out.value);
    }
}
