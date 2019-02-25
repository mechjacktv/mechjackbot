package tv.mechjack.platform.web;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.platform.web.resourcebase.DefaultResourceBaseFactory;

public class WebServerModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ResourceBaseFactory.class)
        .to(DefaultResourceBaseFactory.class)
        .in(Scopes.SINGLETON);
  }

}
