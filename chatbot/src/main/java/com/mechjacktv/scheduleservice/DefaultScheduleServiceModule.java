package com.mechjacktv.scheduleservice;

import com.google.inject.AbstractModule;

public class DefaultScheduleServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ScheduleService.class).to(DefaultScheduleService.class).asEagerSingleton();
  }

}
