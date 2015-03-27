/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.net.URI;
import java.util.Collections;
import java.util.Map.Entry;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class URITransform implements Transform<URI>, TransformFactory {

    private static final Logger LOG = LoggerFactory.getLogger(URITransform.class);

    @Override
    @SuppressWarnings("unchecked")  // Generic array creation
    public Iterable<? extends Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singletonList(Maps.immutableEntry(URI.class, this));
    }

    @Override
    public URI read(String value) throws Exception {
        return new URI(value);
    }

    @Override
    public String write(URI value) throws Exception {
        return String.valueOf(value);
    }
}
