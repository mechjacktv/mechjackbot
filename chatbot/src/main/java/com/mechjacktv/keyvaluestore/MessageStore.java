package com.mechjacktv.keyvaluestore;

import com.google.protobuf.Message;

import java.util.Collection;
import java.util.Optional;

public interface MessageStore<K extends Message, V extends Message> {

  boolean containsKey(K key);

  Collection<K> getKeys();

  Optional<V> get(K key);

  void put(K key, V value);

  void remove(K key);

}
