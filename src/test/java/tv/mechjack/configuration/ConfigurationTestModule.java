package tv.mechjack.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ConfigurationTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(MapConfiguration.class).in(Scopes.SINGLETON);
    this.bind(Configuration.class).to(MapConfiguration.class);
  }

}
