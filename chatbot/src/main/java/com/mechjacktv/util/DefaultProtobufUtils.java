package com.mechjacktv.util;

import com.google.protobuf.Message;

import javax.inject.Inject;
import java.lang.reflect.Method;

final class DefaultProtobufUtils implements ProtobufUtils {

    private final ExecutionUtils executionUtils;

    @Inject
    DefaultProtobufUtils(final ExecutionUtils executionUtils) {
        this.executionUtils = executionUtils;
    }

    @Override
    public final <T extends Message> T parseMessage(final Class<T> messageClass, final byte[] messageBytes) {
        return this.executionUtils.softenException(() -> {
            final Method parseFrom = messageClass.getMethod("parseFrom", byte[].class);

            //noinspection unchecked
            return (T) parseFrom.invoke(null, (Object) messageBytes);
        }); // TODO throw a better exception
    }

}
