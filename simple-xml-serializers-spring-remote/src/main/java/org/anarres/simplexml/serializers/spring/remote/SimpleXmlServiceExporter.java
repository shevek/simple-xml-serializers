/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

/**
 * The primary bean for the RPC server.
 *
 * @author shevek
 */
public class SimpleXmlServiceExporter extends SimpleXmlExporter implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"POST".equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(),
                    new String[]{"POST"}, "SimpleXmlServiceExporter only supports POST requests");
        }

        // request.getHeader(SimpleXmlInvokerRequestExecutor.HTTP_HEADER_ACCEPT_ENCODING, SimpleXmlInvokerRequestExecutor.ENCODING_GZIP);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            invoke(request.getInputStream(), baos);
            response.setContentType(SimpleXmlInvokerRequestExecutor.CONTENT_TYPE_SIMPLEXML);
            response.setContentLength(baos.size());
            baos.writeTo(response.getOutputStream());
        } catch (Throwable e) {
            throw new NestedServletException("SimpleXML skeleton invocation failed", e);
        }
    }
}
