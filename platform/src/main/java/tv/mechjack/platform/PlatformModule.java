package tv.mechjack.platform;

import com.google.inject.AbstractModule;

import tv.mechjack.platform.application.ApplicationModule;
import tv.mechjack.platform.gson.GsonModule;
import tv.mechjack.platform.keyvaluestore.KeyValueStoreModule;
import tv.mechjack.platform.util.UtilModule;
import tv.mechjack.platform.util.scheduleservice.ScheduleServiceModule;

public class PlatformModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new ApplicationModule());
    this.install(new GsonModule());
    this.install(new KeyValueStoreModule());
    this.install(new ScheduleServiceModule());
    this.install(new UtilModule());
  }

}
