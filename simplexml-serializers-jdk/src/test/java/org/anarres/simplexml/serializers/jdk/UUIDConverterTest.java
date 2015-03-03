/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.common.base.Objects;
import java.util.UUID;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class UUIDConverterTest {

    @Root
    public static class UUIDBean {

        @Attribute(required = false)
        public UUID attr;
        @Element(required = false)
        public UUID elem;

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("attr", attr).add("elem", elem).toString();
        }
    }

    @Test
    public void testTransform() throws Exception {
        UUIDBean in = new UUIDBean();
        {
            UUIDBean out = PersisterTestUtils.testSerialization(in);
            assertEquals(in.attr, out.attr);
            assertEquals(in.elem, out.elem);
        }

        in.attr = UUID.randomUUID();
        in.elem = UUID.randomUUID();
        {
            UUIDBean out = PersisterTestUtils.testSerialization(in);
            assertEquals(in.attr, out.attr);
            assertEquals(in.elem, out.elem);
        }

    }
}
