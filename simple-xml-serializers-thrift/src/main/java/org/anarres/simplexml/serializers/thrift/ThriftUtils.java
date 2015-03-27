/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.simplexml.serializers.thrift;

import java.util.Arrays;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author shevek
 */
public class ThriftUtils {

    @Nonnull
    public static byte[] save(@Nonnull TBase<?, ?> object) throws TException {
        // AutoExpandingBufferWriteTransport transport = new AutoExpandingBufferWriteTransport(4096, 1.4);
        TMemoryBuffer transport = new TMemoryBuffer(4096);
        TProtocol protocol = new TCompactProtocol(transport);
        object.write(protocol);
        // return new ByteArray(transport.getBuf().array(), 0, transport.getPos());
        return Arrays.copyOf(transport.getArray(), transport.length());
    }

    @Nonnull
    public static <T extends TBase<?, ?>> T load(@Nonnull T object, @Nonnull byte[] data, @Nonnegative int off, @Nonnegative int len) throws TException {
        TTransport transport = new TMemoryInputTransport(data, off, len);
        TProtocol protocol = new TCompactProtocol(transport);
        object.read(protocol);
        return object;
    }

    @Nonnull
    public static <T extends TBase<?, ?>> T load(@Nonnull T object, @Nonnull byte[] data) throws TException {
        return load(object, data, 0, data.length);
    }

    @Nonnull
    public static <T extends TBase<?, ?>> T load(@Nonnull Class<T> type, @Nonnull byte[] data, @Nonnegative int off, @Nonnegative int len) throws TException {
        try {
            return load(type.newInstance(), data, off, len);
        } catch (InstantiationException e) {
            throw new TException("Failed to instantiate " + type, e);
        } catch (IllegalAccessException e) {
            throw new TException("Failed to instantiate " + type, e);
        }
    }

    @Nonnull
    public static <T extends TBase<?, ?>> T load(@Nonnull Class<T> type, @Nonnull byte[] data) throws TException {
        return load(type, data, 0, data.length);
    }
}
