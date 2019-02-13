package tv.mechjack.platform.webserver;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.platform.webserver.resourcebase.DefaultResourceBaseFactory;

public class WebServerModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ResourceBaseFactory.class)
        .to(DefaultResourceBaseFactory.class)
        .in(Scopes.SINGLETON);
  }

}
