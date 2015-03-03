/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.test;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.anarres.simplexml.factory.PersisterFactory;
import org.anarres.simplexml.factory.PersisterUtils;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class PersisterTestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PersisterTestUtils.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nonnull
    public static <T> T testSerialization(@Nonnull T in) throws IOException {
        return (T) testSerialization((Class) in.getClass(), in);
    }

    @Nonnull
    public static <T> T testSerialization(@Nonnull Class<T> type, @Nonnull T in) throws IOException {
        Persister persister = PersisterFactory.newDefaultPersister();

        LOG.info("Testing serialization of " + type);
        LOG.info("In:  " + in);
        assertNotNull(in);
        byte[] data = PersisterUtils.save(persister, in, false);
        LOG.info("Xml:  " + new String(data));
        T out = PersisterUtils.load(persister, data, type, false);
        LOG.info("Out: " + out);
        assertNotNull(out);
        return out;
    }

    public static <T> void testEquality(T a, T b, boolean result) {
        LOG.info("Testing equality of " + a + (result ? " == " : " != ") + b);
        if (result)
            assertEquals(a + " == " + b, a, b);
        else
            assertNotEquals(a + " != " + b, a, b);
        if (result)
            assertEquals(a.hashCode(), b.hashCode());
    }
}
