package com.mechjacktv.util;

import com.google.protobuf.Message;

public interface ProtobufUtils {

    <T extends Message> T parseMessage(Class<T> messageClass, byte[] messageBytes);

}
