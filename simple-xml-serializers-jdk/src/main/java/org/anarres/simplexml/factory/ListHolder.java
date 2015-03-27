/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.factory;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This allows a List to be serialized into a field annotated with @Element.
 *
 * The alternative is to use a ListConverter, but that breaks the internals of simple-xml.
 *
 * @author shevek
 */
@Root(name = "list")
public class ListHolder implements Holder<List<Object>> {

    @ElementList(name = "items", entry = "item")
    public List<Object> value;

    public ListHolder() {
    }

    public ListHolder(List<Object> value) {
        this.value = value;
    }

    @Override
    public List<Object> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ListHolder(" + value + ")";
    }
}
