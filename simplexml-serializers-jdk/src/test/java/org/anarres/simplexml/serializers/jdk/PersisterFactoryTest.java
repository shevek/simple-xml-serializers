/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.anarres.simplexml.serializers.common.SuppressWarnings;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.junit.Test;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class PersisterFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(PersisterFactoryTest.class);

    @Root
    public static class ArrayBody {

        private final String[] body;

        @SuppressWarnings("EI_EXPOSE_REP2")
        public ArrayBody(@ElementArray(name = "body") String[] body) {
            this.body = body;
        }

        @ElementArray(name = "body", required = false)
        @SuppressWarnings("EI_EXPOSE_REP")
        public String[] getBody() {
            return body;
        }
    }

    @Test
    public void testArrayBody() throws Exception {
        String[] body = new String[]{null, "foo", null, "bar", null};
        ArrayBody in = new ArrayBody(body);
        ArrayBody out = PersisterTestUtils.testSerialization(ArrayBody.class, in);
        LOG.info(Arrays.toString(out.body));
        assertArrayEquals(body, out.body);
    }

    @Test
    public void testList() throws Exception {
        List<String> in = new ArrayList<String>();
        @java.lang.SuppressWarnings("unchecked")
        List<String> out = PersisterTestUtils.testSerialization(List.class, in);
        assertEquals(in, out);
    }

    @Root
    public static class ListBody {

        // @Element fails.
        @ElementList
        private List<String> body;
    }

    @Test
    public void testListBody() throws Exception {
        ListBody in = new ListBody();
        in.body = new ArrayList<String>();
        in.body.add("foo");
        ListBody out = PersisterTestUtils.testSerialization(ListBody.class, in);
        assertEquals(in.body, out.body);
    }

    @Root
    public static class ObjectBody {

        @Element(required = false)
        private Object body;
    }

    private void testObjectBody(Object value) throws Exception {
        ObjectBody in = new ObjectBody();
        in.body = value;
        ObjectBody out = PersisterTestUtils.testSerialization(ObjectBody.class, in);
        assertEquals(in.body, out.body);
    }

    @Test
    public void testObjectBody() throws Throwable {
        testObjectBody(null);
        testObjectBody("foo");
        /* testObjectBody(new long[]{1, 2, 3, 4});
         * List<String> in = new ArrayList<String>();
         * in.add("foo");
         * testObjectBody(in);
         */
    }

    @Root
    public static class LongArrayBody {

        @Attribute(required = false)
        private long[] attr;
        @ElementArray(required = false)
        private long[] body;

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("attr", Arrays.toString(attr))
                    .add("body", Arrays.toString(body))
                    .toString();
        }

    }

    private void testLongArrayBody(long[] value) throws Exception {
        {
            LongArrayBody in = new LongArrayBody();
            in.attr = value;
            LongArrayBody out = PersisterTestUtils.testSerialization(LongArrayBody.class, in);
            assertArrayEquals(value, out.attr);
        }

        {
            LongArrayBody in = new LongArrayBody();
            in.body = value;
            LongArrayBody out = PersisterTestUtils.testSerialization(LongArrayBody.class, in);
            assertArrayEquals(value, out.body);
        }
    }

    @Test
    public void testLongArrayBody() throws Exception {
        testLongArrayBody(null);
        testLongArrayBody(new long[]{1, 2, 3, 4});
    }
}
