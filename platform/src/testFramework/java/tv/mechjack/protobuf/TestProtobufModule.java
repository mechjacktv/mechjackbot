package tv.mechjack.protobuf;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestProtobufModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).in(Scopes.SINGLETON);
  }

}
