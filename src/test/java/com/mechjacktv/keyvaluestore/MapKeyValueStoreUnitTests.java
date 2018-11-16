package com.mechjacktv.keyvaluestore;

import java.util.HashMap;
import java.util.Map;

public class MapKeyValueStoreUnitTests extends KeyValueStoreContractTests {

  @Override
  KeyValueStore givenASubjectToTest(final Map<byte[], byte[]> data) {
    return new MapKeyValueStore(new HashMap<>(data));
  }

}
