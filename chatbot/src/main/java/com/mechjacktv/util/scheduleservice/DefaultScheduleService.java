package com.mechjacktv.util.scheduleservice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class DefaultScheduleService implements ScheduleService {

  private final ScheduledExecutorService scheduledExecutorService;

  DefaultScheduleService() {
    this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
  }

  @Override
  public void schedule(final Runnable runnable, final Integer period, final TimeUnit unit) {
    this.scheduledExecutorService.scheduleAtFixedRate(runnable, 0, period, unit);
  }

  @Override
  public void stop() {
    this.scheduledExecutorService.shutdown();
  }

}
