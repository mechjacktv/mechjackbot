package com.mechjacktv.util.scheduleservice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class DefaultScheduleService implements ScheduleService {

  private static final String POOL_SIZE_KEY = "schedule_service.pool_size";
  private static final int POOL_SIZE = Integer.parseInt(System.getProperty(POOL_SIZE_KEY, "1"));

  private final ScheduledExecutorService scheduledExecutorService;

  DefaultScheduleService() {
    this.scheduledExecutorService = Executors.newScheduledThreadPool(POOL_SIZE);
  }

  @Override
  public void schedule(final Runnable runnable, final Integer period, final TimeUnit unit) {
    schedule(runnable, period, unit, false);
  }

  @Override
  public void schedule(Runnable runnable, Integer period, TimeUnit unit, boolean delay) {
    this.scheduledExecutorService.scheduleAtFixedRate(runnable, delay ? period : 0, period, unit);
  }

  @Override
  public void stop() {
    this.scheduledExecutorService.shutdown();
  }

}
