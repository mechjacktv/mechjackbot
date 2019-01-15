package tv.mechjack.platform.keyvaluestore;

import java.util.Collection;
import java.util.Optional;

import com.google.protobuf.Message;

public interface MessageStore<K extends Message, V extends Message> {

  boolean containsKey(K key);

  Collection<K> getKeys();

  Optional<V> get(K key);

  void put(K key, V value);

  void remove(K key);

}
