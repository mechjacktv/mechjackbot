package tv.mechjack.protobuf;

import tv.mechjack.platform.keyvaluestore.KeyValueStore;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.protobuf.ProtoMessage.TestKeyMessage;
import tv.mechjack.protobuf.ProtoMessage.TestValueMessage;

final class TestMessageStore extends
    BaseMessageStore<TestKeyMessage, TestValueMessage> {

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
