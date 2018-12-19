package com.mechjacktv.keyvaluestore;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.proto.util.UtilsMessage.TestKeyMessage;
import com.mechjacktv.proto.util.UtilsMessage.TestValueMessage;
import com.mechjacktv.util.*;

public class MessageStoreUnitTests extends MessageStoreContractTests<TestKeyMessage, TestValueMessage> {

  @Override
  protected TestMessageStore givenASubjectToTest() {
    return this.givenASubjectToTest(new HashMap<>());
  }

  @Override
  protected TestMessageStore givenASubjectToTest(final Map<TestKeyMessage, TestValueMessage> data) {
    final MapKeyValueStore mapKeyValueStore = new MapKeyValueStore();

    for (final TestKeyMessage key : data.keySet()) {
      mapKeyValueStore.put(key.toByteArray(), data.get(key).toByteArray());
    }
    return new TestMessageStore(mapKeyValueStore, this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(ProtobufUtils.class));
  }

  @Override
  protected TestKeyMessage givenAKey() {
    return this.testFrameworkRule.getInstance(TestKeyMessage.class);
  }

  @Override
  protected TestValueMessage givenAValue() {
    return this.testFrameworkRule.getInstance(TestValueMessage.class);
  }

}
