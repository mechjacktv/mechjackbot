package tv.mechjack.platform.utils;

import java.util.Collection;

import com.google.protobuf.Message;

public interface ProtobufUtils {

  <T extends Message> T parseMessage(Class<T> messageClass, byte[] messageBytes);

  <T extends Message> Collection<T> parseAllMessages(Class<T> messageClass, Collection<byte[]> messageBytesSet);

}
