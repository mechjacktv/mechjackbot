package com.mechjacktv.util.scheduleservice;

import com.google.inject.AbstractModule;

public final class ScheduleServiceModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.bind(ScheduleService.class).to(DefaultScheduleService.class).asEagerSingleton();
  }

}
