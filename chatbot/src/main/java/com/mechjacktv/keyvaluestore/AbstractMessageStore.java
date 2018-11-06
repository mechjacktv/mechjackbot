package com.mechjacktv.keyvaluestore;

import java.util.Collection;
import java.util.Optional;

import com.google.protobuf.Message;

import com.mechjacktv.util.ProtobufUtils;

public abstract class AbstractMessageStore<K extends Message, V extends Message> {

  private final KeyValueStore keyValueStore;
  private final ProtobufUtils protobufUtils;

  protected AbstractMessageStore(final KeyValueStore keyValueStore, final ProtobufUtils protobufUtils) {
    this.keyValueStore = keyValueStore;
    this.protobufUtils = protobufUtils;
  }

  public final boolean containsKey(final K key) {
    return this.keyValueStore.containsKey(key.toByteArray());
  }

  public final Collection<K> getKeys() {
    return this.protobufUtils.parseAllMessages(this.getKeyClass(), this.keyValueStore.getKeys());
  }

  protected abstract Class<K> getKeyClass();

  public final Optional<V> get(final K key) {
    final Optional<byte[]> valueBytes = this.keyValueStore.get(key.toByteArray());

    return valueBytes.map(bytes -> this.protobufUtils.parseMessage(this.getValueClass(), bytes));
  }

  protected abstract Class<V> getValueClass();

  public final void put(final K key, final V value) {
    this.keyValueStore.put(key.toByteArray(), value.toByteArray());
  }

  public final void remove(final K key) {
    this.keyValueStore.remove(key.toByteArray());
  }

}
