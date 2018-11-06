package com.mechjacktv.keyvaluestore;

public interface KeyValueStoreFactory {

    KeyValueStore createOrOpenKeyValueStore(String name);

}
