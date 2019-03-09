package tv.mechjack.platform.webapp;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.platform.webapp.resource.ResourceDispatchModule;
import tv.mechjack.platform.webapp.resourcebase.DefaultResourceBaseFactory;

public class WebAppModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new ResourceDispatchModule());
    this.bind(ResourceBaseFactory.class)
        .to(DefaultResourceBaseFactory.class)
        .in(Scopes.SINGLETON);
  }

}
