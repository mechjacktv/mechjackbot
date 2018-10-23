package com.mechjacktv.util;

import com.google.protobuf.Message;

import java.util.Collection;

public interface ProtobufUtils {

    <T extends Message> Collection<T> parseAllMessages(Class<T> messageClass, Collection<byte[]> messageBytesSet);

    <T extends Message> T parseMessage(Class<T> messageClass, byte[] messageBytes);

}
