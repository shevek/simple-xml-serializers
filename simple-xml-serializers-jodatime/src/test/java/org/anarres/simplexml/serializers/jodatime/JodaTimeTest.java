/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jodatime;

import java.io.IOException;
import org.anarres.simplexml.serializers.test.PersisterTestUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.junit.Test;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class JodaTimeTest {

    private static final Logger LOG = LoggerFactory.getLogger(JodaTimeTest.class);

    public static class Wrapper {

        @Attribute
        public LocalDate ld_attr;
        @Element
        public LocalDate ld_elem;

        @Attribute
        public LocalTime lt_attr;
        @Element
        public LocalTime lt_elem;

        @Attribute
        public LocalDateTime ldt_attr;
        @Element
        public LocalDateTime ldt_elem;

        @Attribute
        public Instant i_attr;
        @Element
        public Instant i_elem;

        @Attribute
        public DateTime dt_attr;
        @Element
        public DateTime dt_elem;

        @Attribute
        public ReadableInstant ri_attr;
        @Element
        public ReadableInstant ri_elem;

        @Attribute
        public Period p_attr;
        @Element
        public Period p_elem;

        @Attribute
        public ReadablePeriod rp_attr;
        @Element
        public ReadablePeriod rp_elem;

        @Override
        public String toString() {
            return ld_attr + " -- " + ld_elem;
        }
    }

    private void testRound(Object in) throws IOException {
        Object out = PersisterTestUtils.testSerialization(in);
        // TODO: assertEquals ?
    }

    @Test
    public void testRound() throws Exception {
        Wrapper obj = new Wrapper();
        obj.ld_attr = new LocalDate().plusDays(10);
        obj.ld_elem = new LocalDate().minusDays(20);
        obj.lt_attr = new LocalTime().plusHours(10);
        obj.lt_elem = new LocalTime().minusHours(20);
        obj.ldt_attr = new LocalDateTime().plusHours(4);
        obj.ldt_elem = new LocalDateTime().minusHours(5);

        obj.dt_attr = new DateTime().plusHours(15);
        obj.dt_elem = new DateTime().minusHours(16);

        obj.i_attr = new Instant(43210000);
        obj.i_elem = new Instant(12348712);
        obj.ri_attr = new Instant(143210000);
        obj.ri_elem = new Instant(112348712);

        obj.p_attr = new Period(123);
        obj.p_elem = new Period(456);
        obj.rp_attr = new Period(789);
        obj.rp_elem = new Period(654);

        testRound(obj.ld_attr);
        testRound(obj.ld_elem);
        testRound(obj.lt_attr);
        testRound(obj.lt_attr);
        testRound(obj.ldt_elem);
        testRound(obj.ldt_elem);
        testRound(obj.dt_attr);
        testRound(obj.dt_elem);
        testRound(obj.i_elem);
        testRound(obj.i_elem);
        testRound(obj);
    }
}
