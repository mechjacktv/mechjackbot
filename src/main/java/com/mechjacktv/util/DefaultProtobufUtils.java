package com.mechjacktv.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import com.google.protobuf.Message;

public final class DefaultProtobufUtils implements ProtobufUtils {

  private final ExecutionUtils executionUtils;

  @Inject
  public DefaultProtobufUtils(final ExecutionUtils executionUtils) {
    this.executionUtils = executionUtils;
  }

  @Override
  public <T extends Message> Collection<T> parseAllMessages(final Class<T> messageClass,
      final Collection<byte[]> messageBytesSet) {
    final Set<T> messages = new HashSet<>();

    for (final byte[] messageBytes : messageBytesSet) {
      messages.add(this.parseMessage(messageClass, messageBytes));
    }
    return messages;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final <T extends Message> T parseMessage(final Class<T> messageClass, final byte[] messageBytes) {
    return this.executionUtils.softenException(() -> {
      final Method parseFrom = messageClass.getMethod("parseFrom", byte[].class);

      return (T) parseFrom.invoke(null, (Object) messageBytes);
    }, MessageParsingException.class);
  }

}
