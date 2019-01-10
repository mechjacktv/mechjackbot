package tv.mechjack.keyvaluestore;

public interface KeyValueStoreFactory {

  KeyValueStore createOrOpenKeyValueStore(String name);

}
