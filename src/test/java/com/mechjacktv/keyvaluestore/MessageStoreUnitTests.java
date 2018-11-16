package com.mechjacktv.keyvaluestore;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import com.mechjacktv.proto.util.UtilsMessage.TestMessage;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultProtobufUtils;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.ProtobufUtils;

public class MessageStoreUnitTests extends MessageStoreContractTest<TestMessage, TestMessage> {

  private static final String DB_NAME = "TEST_DB";

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  TestMessage givenAKey() {
    return this.arbitraryDataGenerator.getTestMessage();
  }

  @Override
  TestMessage givenAValue() {
    return this.arbitraryDataGenerator.getTestMessage();
  }

  @Override
  AbstractMessageStore<TestMessage, TestMessage> givenASubjectToTest(final Map<TestMessage, TestMessage> data) {
    final DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();
    final ConcurrentMap<byte[], byte[]> concurrentMap = db.hashMap(DB_NAME, Serializer.BYTE_ARRAY,
        Serializer.BYTE_ARRAY).createOrOpen();
    final ExecutionUtils executionUtils = new DefaultExecutionUtils();

    for (final TestMessage key : data.keySet()) {
      concurrentMap.put(key.toByteArray(), data.get(key).toByteArray());
    }
    return new TestMessageStore(new MapKeyValueStore(concurrentMap),
        executionUtils, new DefaultProtobufUtils(executionUtils));
  }

  private static final class TestMessageStore extends AbstractMessageStore<TestMessage, TestMessage> {

    TestMessageStore(final KeyValueStore keyValueStore, final ExecutionUtils executionUtils,
        final ProtobufUtils protobufUtils) {
      super(keyValueStore, executionUtils, protobufUtils);
    }

    @Override
    protected Class<TestMessage> getKeyClass() {
      return TestMessage.class;
    }

    @Override
    protected Class<TestMessage> getValueClass() {
      return TestMessage.class;
    }
  }

}
