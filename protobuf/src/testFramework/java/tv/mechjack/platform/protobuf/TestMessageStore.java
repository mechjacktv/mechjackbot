package tv.mechjack.platform.protobuf;

import tv.mechjack.platform.keyvaluestore.KeyValueStore;
import tv.mechjack.platform.protobuf.ProtoMessage.TestKeyMessage;
import tv.mechjack.platform.protobuf.ProtoMessage.TestValueMessage;
import tv.mechjack.platform.utils.ExecutionUtils;

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
