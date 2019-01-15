package tv.mechjack.platform.keyvaluestore;

import tv.mechjack.platform.util.ExecutionUtils;
import tv.mechjack.platform.util.ProtobufUtils;
import tv.mechjack.util.ProtoMessage.TestKeyMessage;
import tv.mechjack.util.ProtoMessage.TestValueMessage;

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
