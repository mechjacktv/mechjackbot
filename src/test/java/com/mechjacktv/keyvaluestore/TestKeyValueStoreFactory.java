package com.mechjacktv.keyvaluestore;

import java.util.HashMap;
import java.util.Map;

public class TestKeyValueStoreFactory implements KeyValueStoreFactory {

  private final Map<String, Map<String, String>> dataMaps;

  TestKeyValueStoreFactory() {
    this.dataMaps = new HashMap<>();
  }

  @Override
  public KeyValueStore createOrOpenKeyValueStore(final String name) {
    if (!this.dataMaps.containsKey(name)) {
      this.setDataMap(name, new HashMap<>());
    }
    return new MapKeyValueStore(this.dataMaps.get(name));
  }

  public final void setDataMap(final String name, final Map<String, String> dataMap) {
    this.dataMaps.put(name, dataMap);
  }

}
