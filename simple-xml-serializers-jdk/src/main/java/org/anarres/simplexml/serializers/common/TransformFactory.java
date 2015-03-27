/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.common;

import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.simpleframework.xml.transform.Transform;

/**
 *
 * @author shevek
 */
public interface TransformFactory {

    @Nonnull
    public Iterable<? extends Entry<? extends Class<?>, ? extends Transform<?>>> newTransforms();
}
