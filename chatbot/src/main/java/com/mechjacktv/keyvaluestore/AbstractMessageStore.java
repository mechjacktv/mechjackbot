package com.mechjacktv.keyvaluestore;

import com.google.protobuf.Message;
import com.mechjacktv.util.ProtobufUtils;

import java.util.Collection;
import java.util.Optional;

public abstract class AbstractMessageStore<K extends Message, V extends Message> implements MessageStore<K, V> {

  protected abstract Class<K> getKeyClass();

  protected abstract Class<V> getValueClass();

  private final KeyValueStore keyValueStore;
  private final ProtobufUtils protobufUtils;

  protected AbstractMessageStore(final KeyValueStore keyValueStore, final ProtobufUtils protobufUtils) {
    this.keyValueStore = keyValueStore;
    this.protobufUtils = protobufUtils;
  }

  @Override
  public boolean containsKey(final K key) {
    return this.keyValueStore.containsKey(key.toByteArray());
  }

  @Override
  public Collection<K> getKeys() {
    return this.protobufUtils.parseAllMessages(this.getKeyClass(), this.keyValueStore.getKeys());
  }

  @Override
  public Optional<V> get(final K key) {
    final Optional<byte[]> valueBytes = this.keyValueStore.get(key.toByteArray());

    return valueBytes.map(bytes -> this.protobufUtils.parseMessage(this.getValueClass(), bytes));
  }

  @Override
  public void put(final K key, final V value) {
    this.keyValueStore.put(key.toByteArray(), value.toByteArray());
  }

  @Override
  public void remove(final K key) {
    this.keyValueStore.remove(key.toByteArray());
  }

}

