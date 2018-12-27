package tv.mechjack.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;

import tv.mechjack.proto.util.UtilsMessage.TestKeyMessage;
import tv.mechjack.proto.util.UtilsMessage.TestValueMessage;
import tv.mechjack.testframework.ArbitraryDataGenerator;
import tv.mechjack.testframework.NullMessageForNameFactory;

public class UtilTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ExecutionUtils.class).to(DefaultExecutionUtils.class).in(Scopes.SINGLETON);
    this.bind(NullMessageForNameFactory.class).to(ExecutionUtilsNullMessageForNameFactory.class).in(Scopes.SINGLETON);
    this.bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).in(Scopes.SINGLETON);
    this.bind(TestTimeUtils.class).in(Scopes.SINGLETON);
    this.bind(TimeUtils.class).to(TestTimeUtils.class);
  }

  @Provides
  public final TestKeyMessage getTestKeyMessage(final ArbitraryDataGenerator arbitraryDataGenerator) {
    return TestKeyMessage.newBuilder().setValue(arbitraryDataGenerator.getString()).build();
  }

  @Provides
  public final TestValueMessage getTestValueMessage(final ArbitraryDataGenerator arbitraryDataGenerator) {
    return TestValueMessage.newBuilder().setValue(arbitraryDataGenerator.getString()).build();
  }

}
