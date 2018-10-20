package com.mechjacktv.mechjackbot;

import java.util.Optional;

public interface KeyValueStore {

    Optional<byte[]> get(byte[] key);

    void put(byte[] key, byte[] value);

    void remove(byte[] key);

}
