/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.factory;

import org.simpleframework.xml.filter.Filter;

/**
 *
 * @author shevek
 */
public class NoopFilter implements Filter {

    public static final NoopFilter INSTANCE = new NoopFilter();

    @Override
    public String replace(String text) {
        return null;
    }
}
