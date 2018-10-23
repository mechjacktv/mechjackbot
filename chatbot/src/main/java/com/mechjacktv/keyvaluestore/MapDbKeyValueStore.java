package com.mechjacktv.keyvaluestore;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

final class MapDbKeyValueStore implements KeyValueStore {

  private final ConcurrentMap<byte[], byte[]> concurrentMap;


  MapDbKeyValueStore(final ConcurrentMap<byte[], byte[]> concurrentMap) {
    this.concurrentMap = concurrentMap;
  }

  @Override
  public boolean containsKey(final byte[] key) {
    return this.concurrentMap.containsKey(key);
  }

  @Override
  public Collection<byte[]> getKeys() {
    return Collections.unmodifiableSet(this.concurrentMap.keySet());
  }

  @Override
  public Optional<byte[]> get(byte[] key) {
    Objects.requireNonNull(key, "`key` MUST NOT be null");

    return Optional.ofNullable(this.concurrentMap.get(key));
  }

  @Override
  public void put(byte[] key, byte[] value) {
    Objects.requireNonNull(key, "`key` MUST NOT be null");
    Objects.requireNonNull(value, "`value` MUST NOT be null");

    if (this.concurrentMap.containsKey(key)) {
      this.concurrentMap.replace(key, value);
    } else {
      this.concurrentMap.put(key, value);
    }
  }

  @Override
  public void remove(byte[] key) {
    Objects.requireNonNull(key, "`key` MUST NOT be null");

    this.concurrentMap.remove(key);
  }
}
