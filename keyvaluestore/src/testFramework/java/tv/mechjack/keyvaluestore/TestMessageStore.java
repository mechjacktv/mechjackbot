package tv.mechjack.keyvaluestore;

import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.ProtoMessage.TestKeyMessage;
import tv.mechjack.util.ProtoMessage.TestValueMessage;
import tv.mechjack.util.ProtobufUtils;

final class TestMessageStore extends BaseMessageStore<TestKeyMessage, TestValueMessage> {

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
