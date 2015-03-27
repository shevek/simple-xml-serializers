/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.factory;

import java.util.Map;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * This allows a Map to be serialized into a field annotated with @Element.
 *
 * The alternative is to use a MapConverter, but that breaks the internals of simple-xml.
 *
 * @author shevek
 */
@Root(name = "map")
public class MapHolder implements Holder<Map<Object, Object>> {

    // Do not make inline or the Map class will be lost.
    @ElementMap(name = "entries", entry = "entry", key = "key", value = "value")
    public Map<Object, Object> value;

    public MapHolder() {
    }

    public MapHolder(Map<Object, Object> value) {
        this.value = value;
    }

    @Override
    public Map<Object, Object> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MapHolder(" + value + ")";
    }
}
