package com.mechjacktv.keyvaluestore;

import java.util.Map;

public class MapKeyValueStoreUnitTests extends KeyValueStoreContractTests {

  @Override
  KeyValueStore givenASubjectToTest(final Map<byte[], byte[]> data) {
    final MapKeyValueStore keyValueStore = new MapKeyValueStore();

    for (final byte[] key : data.keySet()) {
      keyValueStore.put(key, data.get(key));
    }
    return keyValueStore;
  }

}
