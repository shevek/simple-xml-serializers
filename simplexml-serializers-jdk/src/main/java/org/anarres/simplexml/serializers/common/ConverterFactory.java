/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.common;

import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Converter;

/**
 *
 * @author shevek
 */
public interface ConverterFactory {

    @Nonnull
    public Iterable<? extends Entry<? extends Class<?>, ? extends Converter<?>>> newConverters(@Nonnull Serializer serializer);
}
