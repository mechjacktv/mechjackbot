package com.mechjacktv.util;

import java.util.Collection;

import com.google.protobuf.Message;

public interface ProtobufUtils {

    <T extends Message> Collection<T> parseAllMessages(Class<T> messageClass, Collection<byte[]> messageBytesSet);

    <T extends Message> T parseMessage(Class<T> messageClass, byte[] messageBytes);

}
