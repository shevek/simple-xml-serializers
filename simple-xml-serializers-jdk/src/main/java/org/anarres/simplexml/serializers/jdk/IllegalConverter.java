/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.jdk;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 *
 * @author shevek
 */
public class IllegalConverter implements Converter<Object> {

    @Override
    public Object read(InputNode in) throws Exception {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void write(OutputNode on, Object t) throws Exception {
        throw new UnsupportedOperationException("Not supported.");
    }

}
