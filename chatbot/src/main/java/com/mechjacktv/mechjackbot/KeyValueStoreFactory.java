package com.mechjacktv.mechjackbot;

public interface KeyValueStoreFactory {

  KeyValueStore createOrOpenKeyValueStore(String name);

}
