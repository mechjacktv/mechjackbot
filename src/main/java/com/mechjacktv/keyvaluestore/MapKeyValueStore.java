package com.mechjacktv.keyvaluestore;

import java.util.*;

public final class MapKeyValueStore implements KeyValueStore {

  private final Map<byte[], byte[]> dataMap;

  public MapKeyValueStore(final Map<byte[], byte[]> dataMap) {
    this.dataMap = dataMap;
  }

  @Override
  public boolean containsKey(final byte[] key) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");

    return this.dataMap.containsKey(key);
  }

  @Override
  public Collection<byte[]> getKeys() {
    return Collections.unmodifiableSet(this.dataMap.keySet());
  }

  @Override
  public Optional<byte[]> get(byte[] key) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");

    return Optional.ofNullable(this.dataMap.get(key));
  }

  @Override
  public void put(byte[] key, byte[] value) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");
    Objects.requireNonNull(value, "`value` **MUST** not be `null`");

    if (this.dataMap.containsKey(key)) {
      this.dataMap.replace(key, value);
    } else {
      this.dataMap.put(key, value);
    }
  }

  @Override
  public void remove(byte[] key) {
    Objects.requireNonNull(key, "`key` **MUST** not be `null`");

    this.dataMap.remove(key);
  }
}
