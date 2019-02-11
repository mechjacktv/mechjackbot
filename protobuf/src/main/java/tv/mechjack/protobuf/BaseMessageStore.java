package tv.mechjack.protobuf;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import com.google.protobuf.Message;

import tv.mechjack.platform.keyvaluestore.KeyValueStore;
import tv.mechjack.platform.utils.ExecutionUtils;

public abstract class BaseMessageStore<K extends Message, V extends Message> implements MessageStore<K, V> {

  private final KeyValueStore keyValueStore;
  private final ExecutionUtils executionUtils;
  private final ProtobufUtils protobufUtils;

  protected BaseMessageStore(final KeyValueStore keyValueStore, final ExecutionUtils executionUtils,
      final ProtobufUtils protobufUtils) {
    this.keyValueStore = keyValueStore;
    this.executionUtils = executionUtils;
    this.protobufUtils = protobufUtils;
  }

  @Override
  public final boolean containsKey(final K key) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));

    return this.keyValueStore.containsKey(key.toByteArray());
  }

  @Override
  public final Collection<K> getKeys() {
    return this.protobufUtils.parseAllMessages(this.getKeyClass(), this.keyValueStore.getKeys());
  }

  protected abstract Class<K> getKeyClass();

  @Override
  public final Optional<V> get(final K key) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));

    final Optional<byte[]> valueBytes = this.keyValueStore.get(key.toByteArray());

    return valueBytes.map(bytes -> this.protobufUtils.parseMessage(this.getValueClass(), bytes));
  }

  protected abstract Class<V> getValueClass();

  @Override
  public final void put(final K key, final V value) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));
    Objects.requireNonNull(value, this.executionUtils.nullMessageForName("value"));

    this.keyValueStore.put(key.toByteArray(), value.toByteArray());
  }

  @Override
  public final void remove(final K key) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));

    this.keyValueStore.remove(key.toByteArray());
  }

}
