package tv.mechjack.platform.utils.scheduleservice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestScheduleServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(TestScheduleService.class).in(Scopes.SINGLETON);
    this.bind(ScheduleService.class).to(TestScheduleService.class);
  }

}
