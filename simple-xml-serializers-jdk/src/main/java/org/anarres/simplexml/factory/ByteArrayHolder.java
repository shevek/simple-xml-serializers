/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.factory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This allows a List to be serialized into a field annotated with @Element.
 *
 * The alternative is to use a ListConverter, but that breaks the internals of simple-xml.
 *
 * @author shevek
 */
@Root(name = "array")
public class ByteArrayHolder implements Holder<byte[]> {

    @Element(name = "data")
    public byte[] value;

    public ByteArrayHolder() {
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ByteArrayHolder(byte[] value) {
        this.value = value;
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ByteArrayHolder(" + PersisterUtils.toString(value, 0, value.length) + ")";
    }
}
