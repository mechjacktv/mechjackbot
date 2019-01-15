package tv.mechjack.platform;

import com.google.inject.AbstractModule;

import tv.mechjack.platform.application.TestApplicationModule;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.platform.util.TestUtilModule;
import tv.mechjack.platform.util.scheduleservice.TestScheduleServiceModule;

public class TestPlatformModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new TestApplicationModule());
    this.install(new TestKeyValueStoreModule());
    this.install(new TestScheduleServiceModule());
    this.install(new TestUtilModule());
  }

}
