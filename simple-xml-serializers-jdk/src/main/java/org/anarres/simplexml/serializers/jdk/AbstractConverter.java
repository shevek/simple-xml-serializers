/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import org.simpleframework.xml.stream.InputNode;

/**
 *
 * @author shevek
 */
public class AbstractConverter {

    @Nonnull
    protected String getAttribute(InputNode node, String attrname) throws Exception {
        Preconditions.checkNotNull(node, "InputNode was null.");
        InputNode attrnode = node.getAttribute(attrname);
        if (attrnode == null)
            throw new NullPointerException("InputNode has no attribute '" + attrname + "': " + node);
        String attrvalue = attrnode.getValue();
        if (attrvalue == null)
            throw new NullPointerException("InputNode's '" + attrname + "' attribute has no value: " + node);
        return attrvalue;
    }

    @Nonnull
    protected String getValue(InputNode node) throws Exception {
        Preconditions.checkNotNull(node, "InputNode was null.");
        String nodevalue = node.getValue();
        if (nodevalue == null)
            throw new NullPointerException("InputNode has no value: " + node);
        return nodevalue;
    }

    @Nonnull
    protected String getAttributeOrValue(InputNode node, String attrname) throws Exception {
        Preconditions.checkNotNull(node, "InputNode was null.");
        InputNode attrnode = node.getAttribute(attrname);
        if (attrnode == null)
            return getValue(node);
        String attrvalue = attrnode.getValue();
        if (attrvalue == null)
            return getValue(node);
        return attrvalue;
    }
}
