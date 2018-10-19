package com.mechjacktv.util;

import com.google.protobuf.Message;

import java.lang.reflect.Method;

public class ProtobufUtils {

    public <T extends Message> T parseMessage(final Class<T> messageClass, final byte[] messageBytes) {
        try {
            final Method parseFrom = messageClass.getMethod("parseFrom", byte[].class);

            return (T) parseFrom.invoke(null, messageBytes);
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            // TODO throw a better exception
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
