package com.mechjacktv.keyvaluestore;

import java.util.Collection;
import java.util.Optional;

public interface KeyValueStore {

    boolean containsKey(byte[] key);

    Collection<byte[]> getKeys();

    Optional<byte[]> get(byte[] key);

    void put(byte[] key, byte[] value);

    void remove(byte[] key);

}
