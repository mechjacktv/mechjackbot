package com.mechjacktv.util;

import com.google.protobuf.Message;

import java.lang.reflect.Method;

final class DefaultProtobufUtils implements ProtobufUtils {

    @Override
    public final <T extends Message> T parseMessage(final Class<T> messageClass, final byte[] messageBytes) {
        try {
            final Method parseFrom = messageClass.getMethod("parseFrom", byte[].class);
            //noinspection unchecked
            return (T) parseFrom.invoke(null, (Object) messageBytes);
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            // TODO throw a better exception
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
