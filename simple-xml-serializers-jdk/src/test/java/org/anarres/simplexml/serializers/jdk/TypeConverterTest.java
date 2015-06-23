/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.common.base.Charsets;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.anarres.typeserializer.core.impl.ParameterizedTypeImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class TypeConverterTest {

    private static final Logger LOG = LoggerFactory.getLogger(TypeConverterTest.class);

    @Root
    public static class TypeBean {

        @Attribute
        public Type attr;
        @Element
        public Type elem;
    }

    @Root
    public static class ClassBean {

        @Attribute
        public Class<?> attr;
        @Element
        public Class<?> elem;
    }

    @Root
    public static class ArrayBean {

        @ElementArray
        public Type[] types;
        @ElementArray
        public Class<?>[] classes;
    }

    @Root
    public static class MethodArrayBean {

        public final Type[] types;
        public final Class<?>[] classes;

        @SuppressFBWarnings("EI_EXPOSE_REP2")
        public MethodArrayBean(
                @ElementArray(name = "types", entry = "type", empty = false) Type[] types,
                @ElementArray(name = "classes", entry = "class", empty = false) Class<?>[] classes) {
            this.types = types;
            this.classes = classes;
        }

        @ElementArray(name = "types", entry = "type", empty = false)
        @SuppressFBWarnings("EI_EXPOSE_REP")
        public Type[] getTypes() {
            return types;
        }

        @ElementArray(name = "classes", entry = "class", empty = false)
        @SuppressFBWarnings("EI_EXPOSE_REP")
        public Class<?>[] getClasses() {
            return classes;
        }
    }

    @Nonnull
    private static RegistryMatcher newRegistryMatcher() {
        RegistryMatcher matcher = new RegistryMatcher() {
            @Override
            public Transform match(Class type) throws Exception {
                if (Type.class.isAssignableFrom(type))
                    return TypeTransform.getInstance();
                return super.match(type);
            }
        };
        return matcher;
    }

    @Nonnull
    public static <T> T testSerialization(@Nonnull Class<T> type, @Nonnull T in) throws Exception {
        Persister persister = new Persister(new AnnotationStrategy(), newRegistryMatcher());

        LOG.info("In:  " + in);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        persister.write(in, os);
        byte[] data = os.toByteArray();
        LOG.info("Xml:  " + new String(data, Charsets.UTF_8));

        ByteArrayInputStream is = new ByteArrayInputStream(data);
        T out = persister.read(type, is);
        LOG.info("Out: " + out);

        return out;
    }

    @Ignore // Doesn't work as a top level bean.
    @Test
    public void testConverter() throws Exception {
        Type in = new ParameterizedTypeImpl(Map.class, String.class, String.class);
        Type out = testSerialization(Type.class, in);
        assertEquals(in, out);
    }

    @Test
    public void testTypeBean() throws Exception {
        TypeBean in = new TypeBean();
        in.attr = new ParameterizedTypeImpl(List.class, Integer.class);
        in.elem = new ParameterizedTypeImpl(Map.class, Long.class, Double.class);
        TypeBean out = testSerialization(TypeBean.class, in);
        assertEquals(in.attr, out.attr);
        assertEquals(in.elem, out.elem);
    }

    @Test
    public void testClassBean() throws Exception {
        for (Class<?> type : new Class<?>[]{Integer.class, int.class, Map.class}) {
            ClassBean in = new ClassBean();
            in.attr = type;
            in.elem = type;
            ClassBean out = testSerialization(ClassBean.class, in);
            assertEquals(in.attr, out.attr);
            assertEquals(in.elem, out.elem);
        }
    }

    @Test
    public void testClassArrayBean() throws Exception {

        if (false) {
            ArrayBean in = new ArrayBean();
            ArrayBean out = testSerialization(ArrayBean.class, in);
            assertArrayEquals(in.types, out.types);
            assertArrayEquals(in.classes, out.classes);
        }

        {
            ArrayBean in = new ArrayBean();
            in.types = new Type[]{};
            in.classes = new Class<?>[]{};
            ArrayBean out = testSerialization(ArrayBean.class, in);
            assertArrayEquals(in.types, out.types);
            assertArrayEquals(in.classes, out.classes);
        }

        for (Class<?> type : new Class<?>[]{Integer.class, int.class, Map.class}) {
            ArrayBean in = new ArrayBean();
            in.types = new Type[]{type, int.class};
            in.classes = new Class<?>[]{type, int.class};
            ArrayBean out = testSerialization(ArrayBean.class, in);
            assertArrayEquals(in.types, out.types);
            assertArrayEquals(in.classes, out.classes);
        }
    }

    @Test
    public void testMethodArrayBean() throws Exception {

        {
            MethodArrayBean in = new MethodArrayBean(new Type[]{}, new Class<?>[]{});
            MethodArrayBean out = testSerialization(MethodArrayBean.class, in);
            assertArrayEquals(in.types, out.types);
        }

        for (Class<?> type : new Class<?>[]{Integer.class, int.class, Map.class}) {
            MethodArrayBean in = new MethodArrayBean(new Type[]{type, int.class}, new Class<?>[]{type, int.class});
            MethodArrayBean out = testSerialization(MethodArrayBean.class, in);
            assertArrayEquals(in.types, out.types);
        }
    }
}
