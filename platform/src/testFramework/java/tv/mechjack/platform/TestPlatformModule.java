package tv.mechjack.platform;

import com.google.inject.AbstractModule;

import tv.mechjack.platform.application.TestApplicationModule;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleServiceModule;

public class TestPlatformModule extends AbstractModule {

  @Override
  protected void configure() {
    this.install(new TestApplicationModule());
    this.install(new TestConfigurationModule());
    this.install(new TestKeyValueStoreModule());
    this.install(new TestScheduleServiceModule());
    this.install(new TestUtilsModule());
  }

}
