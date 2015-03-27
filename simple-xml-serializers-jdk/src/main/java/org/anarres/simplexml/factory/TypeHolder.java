/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.factory;

import java.lang.reflect.Type;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 *
 * @author shevek
 */
@Root(name = "type")
public class TypeHolder implements Holder<Type> {

    @Attribute(name = "type")
    public Type value;

    public TypeHolder() {
    }

    public TypeHolder(Type value) {
        this.value = value;
    }

    @Override
    public Type getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TypeHolder(" + value + ")";
    }
}
