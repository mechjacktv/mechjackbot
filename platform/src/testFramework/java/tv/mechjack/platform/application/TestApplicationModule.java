package tv.mechjack.platform.application;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestApplicationModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestApplication.class).in(Scopes.SINGLETON);
    this.bind(Application.class).to(TestApplication.class);
  }

}
