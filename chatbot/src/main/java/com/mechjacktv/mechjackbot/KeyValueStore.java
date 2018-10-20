package com.mechjacktv.mechjackbot;

import java.util.Collection;
import java.util.Optional;

public interface KeyValueStore {

    Collection<byte[]> getKeys();

    Optional<byte[]> get(byte[] key);

    void put(byte[] key, byte[] value);

    void remove(byte[] key);

}
