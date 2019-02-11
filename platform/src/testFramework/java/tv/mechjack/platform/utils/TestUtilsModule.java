package tv.mechjack.platform.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;

import tv.mechjack.protobuf.DefaultProtobufUtils;
import tv.mechjack.protobuf.ProtoMessage.TestKeyMessage;
import tv.mechjack.protobuf.ProtoMessage.TestValueMessage;
import tv.mechjack.protobuf.ProtobufUtils;
import tv.mechjack.testframework.ArbitraryData;

public class TestUtilsModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ExecutionUtils.class).to(DefaultExecutionUtils.class).in(Scopes.SINGLETON);
    this.bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).in(Scopes.SINGLETON);
    this.bind(TestRandomUtils.class).in(Scopes.SINGLETON);
    this.bind(RandomUtils.class).to(TestRandomUtils.class);
    this.bind(TestTimeUtils.class).in(Scopes.SINGLETON);
    this.bind(TimeUtils.class).to(TestTimeUtils.class);
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
