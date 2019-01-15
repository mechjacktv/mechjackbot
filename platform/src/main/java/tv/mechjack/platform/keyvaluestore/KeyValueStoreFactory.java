package tv.mechjack.platform.keyvaluestore;

public interface KeyValueStoreFactory {

  KeyValueStore createOrOpenKeyValueStore(String name);

}
