package tv.mechjack.platform.protobuf;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;

import tv.mechjack.platform.protobuf.ProtoMessage.TestKeyMessage;
import tv.mechjack.platform.protobuf.ProtoMessage.TestValueMessage;
import tv.mechjack.testframework.ArbitraryData;

public class TestProtobufModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).in(Scopes.SINGLETON);
  }

  @Provides
  public final TestKeyMessage getTestKeyMessage(final ArbitraryData arbitraryDataGenerator) {
    return TestKeyMessage.newBuilder().setValue(arbitraryDataGenerator.getString()).build();
  }

  @Provides
  public final TestValueMessage getTestValueMessage(final ArbitraryData arbitraryDataGenerator) {
    return TestValueMessage.newBuilder().setValue(arbitraryDataGenerator.getString()).build();
  }

}
