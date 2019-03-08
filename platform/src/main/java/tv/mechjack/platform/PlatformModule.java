package tv.mechjack.platform;

import com.google.inject.AbstractModule;

import tv.mechjack.platform.application.ApplicationModule;
import tv.mechjack.platform.gson.GsonModule;
import tv.mechjack.platform.keyvaluestore.KeyValueStoreModule;
import tv.mechjack.platform.utils.UtilsModule;
import tv.mechjack.platform.utils.scheduleservice.ScheduleServiceModule;
import tv.mechjack.platform.webapp.jetty.JettyModule;

public class PlatformModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new ApplicationModule());
    this.install(new GsonModule());
    this.install(new JettyModule());
    this.install(new KeyValueStoreModule());
    this.install(new ScheduleServiceModule());
    this.install(new UtilsModule());
  }

}
