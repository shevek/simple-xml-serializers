/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.factory;

import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import javax.annotation.Nonnull;
import org.anarres.simplexml.serializers.common.ConverterFactory;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.anarres.simplexml.serializers.jdk.SerializableTransform;
import org.anarres.typeserializer.simplexml.TypeConverter;
import org.anarres.typeserializer.simplexml.TypeTransform;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.CycleStrategy;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class PersisterFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PersisterFactory.class);
    public static final String ID = "_xid";
    public static final String REF = "_xref";

    private final List<ConverterFactory> converterFactories = new ArrayList<ConverterFactory>();

    public void addConverters(@Nonnull Iterable<? extends ConverterFactory> converterFactories) {
        Iterables.addAll(this.converterFactories, converterFactories);
    }

    public void addConverters(@Nonnull ConverterFactory... converterFactories) {
        addConverters(Arrays.asList(converterFactories));
    }

    private static class SuperClassLookupRegistry extends Registry {

        private final List<Class<?>> superclasses = new ArrayList<Class<?>>();

        @Override
        @SuppressWarnings("rawtypes")
        public Converter lookup(Class type) throws Exception {
            Converter converter = super.lookup(type);
            if (converter == null) {
                if (Type.class.isAssignableFrom(type))
                    return TypeConverter.getInstance();
                else if (Charset.class.isAssignableFrom(type))
                    return super.lookup(Charset.class);
                // This cannot be Serializable because that would cause ArrayList etc to get b0rked.
                else if (Throwable.class.isAssignableFrom(type)) // Includes Throwable.
                    return super.lookup(Serializable.class);
                // else if (TBase.class.isAssignableFrom(type)) return super.lookup(TBase.class);
                // bind(type, converter);
            }
            // LOG.info("Convert " + type + " using " + converter, new Exception());
            return converter;
        }
    }

    private static void addConverters(@Nonnull Registry registry, @Nonnull Serializer serializer, @Nonnull Iterable<? extends ConverterFactory> factories) {
        // LOG.info("Loading ConverterFactory instances.");
        for (ConverterFactory factory : factories) {
            // LOG.info("Factory " + factory);
            for (Map.Entry<? extends Class<?>, ? extends Converter<?>> e : factory.newConverters(serializer)) {
                // LOG.info("Register " + e.getKey() + " -> " + e.getValue());
                try {
                    registry.bind(e.getKey(), e.getValue());
                } catch (Exception ex) {
                    LOG.error("Failed to bind " + e.getKey() + " to " + e.getValue(), ex);
                }
            }
        }
    }

    private final List<TransformFactory> transformFactories = new ArrayList<TransformFactory>();

    public void addTransforms(@Nonnull Iterable<? extends TransformFactory> transformFactories) {
        Iterables.addAll(this.transformFactories, transformFactories);
    }

    public void addTransforms(@Nonnull TransformFactory... transformFactories) {
        addTransforms(Arrays.asList(transformFactories));
    }

    private static class SuperClassLookupRegistryMatcher extends RegistryMatcher {

        @Override
        @SuppressWarnings("rawtypes")
        public Transform match(Class type) throws Exception {
            Transform transform = super.match(type);
            if (transform == null) {
                if (Type.class.isAssignableFrom(type))
                    transform = TypeTransform.getInstance();
                else if (Throwable.class.isAssignableFrom(type))
                    transform = SerializableTransform.getInstance();
                else if (Charset.class.isAssignableFrom(type))
                    return super.match(Charset.class);
                // bind(type, transform);
            }
            // LOG.info("Transform " + type + " using " + transform, new Exception());
            return transform;
        }
    }

    private static void addTransformers(@Nonnull RegistryMatcher matcher, @Nonnull Serializer serializer, @Nonnull Iterable<? extends TransformFactory> factories) {
        for (TransformFactory factory : factories) {
            // LOG.info("Factory " + factory);
            for (Map.Entry<? extends Class<?>, ? extends Transform<?>> e : factory.newTransforms()) {
                // LOG.info("Register " + e.getKey() + " -> " + e.getValue());
                try {
                    matcher.bind(e.getKey(), e.getValue());
                } catch (Exception ex) {
                    LOG.error("Failed to bind " + e.getKey() + " to " + e.getValue(), ex);
                }
            }
        }
    }

    @Nonnull
    public Persister newPersister() {
        Registry registry = new SuperClassLookupRegistry();
        RegistryMatcher matcher = new SuperClassLookupRegistryMatcher();

        Strategy strategy = new CycleStrategy(ID, REF);
        strategy = new AnnotationStrategy(strategy);
        strategy = new RegistryStrategy(registry, strategy);
        Persister persister = new Persister(strategy, NoopFilter.INSTANCE, matcher);

        addTransformers(matcher, persister, transformFactories);
        addConverters(registry, persister, converterFactories);
        return persister;
    }

    @Nonnull
    public static Persister newDefaultPersister() {
        PersisterFactory factory = new PersisterFactory();
        factory.addConverters(ServiceLoader.load(ConverterFactory.class));
        factory.addTransforms(ServiceLoader.load(TransformFactory.class));
        return factory.newPersister();
    }
}
