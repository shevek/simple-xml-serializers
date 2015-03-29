/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import javax.annotation.Nonnull;
import org.simpleframework.xml.Serializer;

/**
 *
 * @author shevek
 */
public interface SimpleXmlInvokerClientConfiguration {

    @Nonnull
    public String getServiceUrl();

    @Nonnull
    public Serializer getSerializer();
}
