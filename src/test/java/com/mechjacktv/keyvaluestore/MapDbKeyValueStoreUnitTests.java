package com.mechjacktv.keyvaluestore;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class MapDbKeyValueStoreUnitTests extends KeyValueStoreContractTests {

  private static final String DB_NAME = "TEST_DB";

  @Override
  KeyValueStore givenASubjectToTest(Map<byte[], byte[]> data) {
    final DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();
    final ConcurrentMap<byte[], byte[]> concurrentMap = db.hashMap(DB_NAME, Serializer.BYTE_ARRAY,
        Serializer.BYTE_ARRAY).createOrOpen();

    for (final byte[] key : data.keySet()) {
      concurrentMap.put(key, data.get(key));
    }
    return new MapDbKeyValueStore(concurrentMap);
  }

}
