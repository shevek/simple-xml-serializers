package org.anarres.simplexml.serializers.jdk;

import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import org.anarres.simplexml.serializers.common.TransformFactory;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
@AutoService(TransformFactory.class)
public class CharsetTransform implements Transform<Charset>, TransformFactory {

    @Override
    public Iterable<? extends Map.Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms() {
        return Collections.singleton(Maps.immutableEntry(Charset.class, this));
    }

    @Override
    public Charset read(String string) throws Exception {
        return Charset.forName(string);
    }

    @Override
    public String write(Charset t) throws Exception {
        return t.name();
    }
}
