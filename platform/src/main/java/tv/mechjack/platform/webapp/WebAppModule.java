package tv.mechjack.platform.webapp;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import tv.mechjack.platform.webapp.resourcebase.DefaultResourceBaseFactory;
import tv.mechjack.platform.webapp.services.WebAppServicesModule;

public class WebAppModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new WebAppServicesModule());
    this.bind(ResourceBaseFactory.class)
        .to(DefaultResourceBaseFactory.class)
        .in(Scopes.SINGLETON);
  }

}
