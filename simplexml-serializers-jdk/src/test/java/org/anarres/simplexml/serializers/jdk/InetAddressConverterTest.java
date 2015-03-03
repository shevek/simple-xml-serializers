/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.simpleframework.xml.Element;

/**
 *
 * @author shevek
 */
public class InetAddressConverterTest {

    private static final Logger LOG = LoggerFactory.getLogger(InetAddressConverterTest.class);

    public static class Wrapper {

        @Element
        public InetAddress addr;
        @Element
        public Inet4Address addr4;
        @Element
        public Inet6Address addr6;

        @Override
        public String toString() {
            return addr + " -- " + addr4 + " -- " + addr6;
        }
    }

    private void testRound(Object in) throws IOException {
        Object out = PersisterTestUtils.testSerialization(in);
        // TODO: assertEquals ?
    }

    @Test
    public void testRound() throws Exception {
        Wrapper obj = new Wrapper();
        obj.addr = InetAddress.getLocalHost();
        obj.addr4 = (Inet4Address) Inet4Address.getByAddress(new byte[]{1, 2, 3, 4});
        obj.addr6 = Inet6Address.getByAddress(null, new byte[]{6, 5, 4, 3, 2, 1, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1}, -1);

        testRound(obj.addr);
        testRound(obj.addr4);
        testRound(obj.addr6);
        testRound(obj);
    }
}
