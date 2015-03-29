/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.spring.remote;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 *
 * @author shevek
 */
public class SimpleXmlInvokerRequestExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleXmlInvokerRequestExecutor.class);
    public static final String CONTENT_TYPE_SIMPLEXML = "application/x-simple-xml";
    protected static final String HTTP_METHOD_POST = "POST";
    protected static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    protected static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    protected static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
    protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
    protected static final String ENCODING_GZIP = "gzip";
    private static final int SERIALIZED_INVOCATION_BYTE_ARRAY_INITIAL_SIZE = 1024;
    private String contentType = CONTENT_TYPE_SIMPLEXML;
    private boolean acceptGzipEncoding = true;
    private int connectTimeout = -1;
    private int readTimeout = -1;

    /**
     * Specify the content type to use for sending HTTP invoker requests.
     * <p>
     * Default is "application/x-java-serialized-object".
     */
    public void setContentType(String contentType) {
        Assert.notNull(contentType, "'contentType' must not be null");
        this.contentType = contentType;
    }

    /**
     * Return the content type to use for sending HTTP invoker requests.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Set whether to accept GZIP encoding, that is, whether to
     * send the HTTP "Accept-Encoding" header with "gzip" as value.
     * <p>
     * Default is "true". Turn this flag off if you do not want
     * GZIP response compression even if enabled on the HTTP server.
     */
    public void setAcceptGzipEncoding(boolean acceptGzipEncoding) {
        this.acceptGzipEncoding = acceptGzipEncoding;
    }

    /**
     * Return whether to accept GZIP encoding, that is, whether to
     * send the HTTP "Accept-Encoding" header with "gzip" as value.
     */
    public boolean isAcceptGzipEncoding() {
        return this.acceptGzipEncoding;
    }

    /**
     * Set the underlying URLConnection's connect timeout (in milliseconds).
     * A timeout value of 0 specifies an infinite timeout.
     * <p>
     * Default is the system's default timeout.
     *
     * @see URLConnection#setConnectTimeout(int)
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Set the underlying URLConnection's read timeout (in milliseconds).
     * A timeout value of 0 specifies an infinite timeout.
     * <p>
     * Default is the system's default timeout.
     *
     * @see URLConnection#setReadTimeout(int)
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * Execute the given request through a standard J2SE HttpURLConnection.
     * <p>
     * This method implements the basic processing workflow:
     * The actual work happens in this class's template methods.
     *
     * @see #openConnection
     * @see #prepareConnection
     * @see #writeRequestBody
     * @see #validateResponse
     * @see #readResponseBody
     */
    public final SimpleXmlInvocationResult executeRequest(SimpleXmlInvokerClientConfiguration config, SimpleXmlInvocation invocation) throws Exception {
        ByteArrayOutputStream baos = toByteArrayOutputStream(config, invocation);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending HTTP invoker request for service at [" + config.getServiceUrl()
                    + "], with size " + baos.size());
        }
        return doExecuteRequest(config, baos);
    }

    /**
     * Serialize the given SimpleXmlInvocation into a ByteArrayOutputStream.
     *
     * @param invocation the SimpleXmlInvocation object
     * @return a ByteArrayOutputStream with the serialized SimpleXmlInvocation
     * @throws IOException if thrown by I/O methods
     */
    protected ByteArrayOutputStream toByteArrayOutputStream(SimpleXmlInvokerClientConfiguration config, SimpleXmlInvocation invocation) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(SERIALIZED_INVOCATION_BYTE_ARRAY_INITIAL_SIZE);
        writeSimpleXmlInvocation(config, invocation, baos);
        baos.write('\n');   // Makes debugging come out nicer with CommonsLogWriter.
        return baos;
    }

    private void writeSimpleXmlInvocation(SimpleXmlInvokerClientConfiguration config, SimpleXmlInvocation invocation, OutputStream baos) throws Exception {
        /*
         if (LOG.isDebugEnabled()) {
         Writer writer = new Slf4jLogWriter(LOG);
         WriterOutputStream stream = new WriterOutputStream(writer);
         baos = new TeeOutputStream(baos, stream);
         }
         */
        config.getSerializer().write(invocation, baos);
        baos.flush();
    }

    @Nonnull
    protected SimpleXmlInvocationResult doExecuteRequest(
            SimpleXmlInvokerClientConfiguration config, ByteArrayOutputStream baos)
            throws Exception {

        HttpURLConnection con = openConnection(config);
        try {
            prepareConnection(con, baos.size());
            writeRequestBody(config, con, baos);
            validateResponse(config, con);
            InputStream responseBody = readResponseBody(config, con);

            return readSimpleXmlInvocationResult(config, responseBody);
        } finally {
            con.disconnect();
        }
    }

    @Nonnull
    protected SimpleXmlInvocationResult readSimpleXmlInvocationResult(@Nonnull SimpleXmlInvokerClientConfiguration config, @Nonnull InputStream responseBody) throws Exception {
        byte[] data = ByteStreams.toByteArray(responseBody);
        LOG.info("Response is " + new String(data, Charsets.ISO_8859_1));   // Random 8-bit charset.
        responseBody = new ByteArrayInputStream(data);
        /*
         if (false && LOG.isDebugEnabled()) {
         Writer writer = new Slf4jLogWriter(LOG);
         WriterOutputStream stream = new WriterOutputStream(writer);
         responseBody = new TeeInputStream(responseBody, stream, true);
         }
         */
        // LOG.debug("About to read response.");
        try {
            SimpleXmlInvocationResult result = config.getSerializer().read(SimpleXmlInvocationResult.class, responseBody, true);
            LOG.info("Result is " + result);
            return result;
        } finally {
            Closeables.close(responseBody, true);
            // LOG.debug("Done reading response.");
        }
    }

    /**
     * Open an HttpURLConnection for the given remote invocation request.
     *
     * @param config the HTTP invoker configuration that specifies the
     * target service
     * @return the HttpURLConnection for the given request
     * @throws IOException if thrown by I/O methods
     * @see java.net.URL#openConnection()
     */
    protected HttpURLConnection openConnection(SimpleXmlInvokerClientConfiguration config) throws IOException {
        URLConnection con = new URL(config.getServiceUrl()).openConnection();
        if (!(con instanceof HttpURLConnection))
            throw new IOException("Service URL [" + config.getServiceUrl() + "] is not an HTTP URL");
        return (HttpURLConnection) con;
    }

    /**
     * Prepare the given HTTP connection.
     * <p>
     * The default implementation specifies POST as method,
     * "application/x-java-serialized-object" as "Content-Type" header,
     * and the given content length as "Content-Length" header.
     *
     * @param connection the HTTP connection to prepare
     * @param contentLength the length of the content to send
     * @throws IOException if thrown by HttpURLConnection methods
     * @see java.net.HttpURLConnection#setRequestMethod
     * @see java.net.HttpURLConnection#setRequestProperty
     */
    protected void prepareConnection(HttpURLConnection connection, int contentLength) throws IOException {
        if (this.connectTimeout >= 0)
            connection.setConnectTimeout(this.connectTimeout);
        if (this.readTimeout >= 0)
            connection.setReadTimeout(this.readTimeout);
        connection.setDoOutput(true);
        connection.setRequestMethod(HTTP_METHOD_POST);
        connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, getContentType());
        connection.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH, Integer.toString(contentLength));
        LocaleContext locale = LocaleContextHolder.getLocaleContext();
        if (locale != null)
            connection.setRequestProperty(HTTP_HEADER_ACCEPT_LANGUAGE, StringUtils.toLanguageTag(locale.getLocale()));
        if (isAcceptGzipEncoding())
            connection.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
    }

    /**
     * Set the given serialized remote invocation as request body.
     * <p>
     * The default implementation simply write the serialized invocation to the
     * HttpURLConnection's OutputStream. This can be overridden, for example, to write
     * a specific encoding and potentially set appropriate HTTP request headers.
     *
     * @param config the HTTP invoker configuration that specifies the target service
     * @param con the HttpURLConnection to write the request body to
     * @param baos the ByteArrayOutputStream that contains the serialized
     * SimpleXmlInvocation object
     * @throws IOException if thrown by I/O methods
     * @see java.net.HttpURLConnection#getOutputStream()
     * @see java.net.HttpURLConnection#setRequestProperty
     */
    protected void writeRequestBody(
            SimpleXmlInvokerClientConfiguration config, HttpURLConnection con, ByteArrayOutputStream baos)
            throws IOException {
        baos.writeTo(con.getOutputStream());
    }

    /**
     * Validate the given response as contained in the HttpURLConnection object,
     * throwing an exception if it does not correspond to a successful HTTP response.
     * <p>
     * Default implementation rejects any HTTP status code beyond 2xx, to avoid
     * parsing the response body and trying to deserialize from a corrupted stream.
     *
     * @param config the HTTP invoker configuration that specifies the target service
     * @param con the HttpURLConnection to validate
     * @throws IOException if validation failed
     * @see java.net.HttpURLConnection#getResponseCode()
     */
    protected void validateResponse(SimpleXmlInvokerClientConfiguration config, HttpURLConnection con)
            throws IOException {

        int responseCode = con.getResponseCode();
        if (responseCode >= 300) {
            throw new IOException(
                    "Did not receive successful HTTP response: status code = " + responseCode
                    + ", status message = [" + con.getResponseMessage() + "]");
        }
    }

    /**
     * Extract the response body from the given executed remote invocation
     * request.
     * <p>
     * The default implementation simply reads the serialized invocation
     * from the HttpURLConnection's InputStream. If the response is recognized
     * as GZIP response, the InputStream will get wrapped in a GZIPInputStream.
     *
     * @param config the HTTP invoker configuration that specifies the target service
     * @param con the HttpURLConnection to read the response body from
     * @return an InputStream for the response body
     * @throws IOException if thrown by I/O methods
     * @see #isGzipResponse
     * @see java.util.zip.GZIPInputStream
     * @see java.net.HttpURLConnection#getInputStream()
     * @see java.net.HttpURLConnection#getHeaderField(int)
     * @see java.net.HttpURLConnection#getHeaderFieldKey(int)
     */
    protected InputStream readResponseBody(SimpleXmlInvokerClientConfiguration config, HttpURLConnection con)
            throws IOException {

        if (isGzipResponse(con)) {
            // GZIP response found - need to unzip.
            return new GZIPInputStream(con.getInputStream());
        } else {
            // Plain response found.
            return con.getInputStream();
        }
    }

    /**
     * Determine whether the given response is a GZIP response.
     * <p>
     * Default implementation checks whether the HTTP "Content-Encoding"
     * header contains "gzip" (in any casing).
     *
     * @param con the HttpURLConnection to check
     */
    protected boolean isGzipResponse(HttpURLConnection con) {
        String encodingHeader = con.getHeaderField(HTTP_HEADER_CONTENT_ENCODING);
        return (encodingHeader != null && encodingHeader.toLowerCase().contains(ENCODING_GZIP));
    }
}
