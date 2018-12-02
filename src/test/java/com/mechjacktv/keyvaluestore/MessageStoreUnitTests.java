package com.mechjacktv.keyvaluestore;

import java.util.Map;

import com.mechjacktv.proto.util.UtilsMessage.TestKeyMessage;
import com.mechjacktv.proto.util.UtilsMessage.TestValueMessage;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.util.*;

public class MessageStoreUnitTests extends MessageStoreContractTests<TestKeyMessage, TestValueMessage> {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected TestKeyMessage givenAKey() {
    return this.arbitraryDataGenerator.getTestKeyMessage();
  }

  @Override
  protected TestValueMessage givenAValue() {
    return this.arbitraryDataGenerator.getTestValueMessage();
  }

  @Override
  protected AbstractMessageStore<TestKeyMessage, TestValueMessage> givenASubjectToTest(
      final Map<TestKeyMessage, TestValueMessage> data) {
    final MapKeyValueStore mapKeyValueStore = new MapKeyValueStore();
    final ExecutionUtils executionUtils = new DefaultExecutionUtils();

    for (final TestKeyMessage key : data.keySet()) {
      mapKeyValueStore.put(key.toByteArray(), data.get(key).toByteArray());
    }
    return new TestMessageStore(mapKeyValueStore, executionUtils, new DefaultProtobufUtils(executionUtils));
  }

  private static final class TestMessageStore extends AbstractMessageStore<TestKeyMessage, TestValueMessage> {

    TestMessageStore(final KeyValueStore keyValueStore, final ExecutionUtils executionUtils,
        final ProtobufUtils protobufUtils) {
      super(keyValueStore, executionUtils, protobufUtils);
    }

    @Override
    protected Class<TestKeyMessage> getKeyClass() {
      return TestKeyMessage.class;
    }

    @Override
    protected Class<TestValueMessage> getValueClass() {
      return TestValueMessage.class;
    }
  }

}
