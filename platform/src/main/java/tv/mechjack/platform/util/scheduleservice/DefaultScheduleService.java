package tv.mechjack.platform.util.scheduleservice;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public final class DefaultScheduleService implements ScheduleService {

  private static final String POOL_SIZE_KEY = "schedule_service.pool_size";
  private static final int POOL_SIZE = Integer.parseInt(System.getProperty(POOL_SIZE_KEY, "1"));

  private final ScheduledExecutorService scheduledExecutorService;

  @Inject
  DefaultScheduleService() {
    this(Executors.newScheduledThreadPool(POOL_SIZE));
  }

  DefaultScheduleService(final ScheduledExecutorService scheduledExecutorService) {
    this.scheduledExecutorService = scheduledExecutorService;
  }

  @Override
  public void schedule(final Runnable runnable, final Integer period, final TimeUnit unit) {
    schedule(runnable, period, unit, false);
  }

  @Override
  public void schedule(Runnable runnable, Integer period, TimeUnit unit, Boolean delay) {
    Objects.requireNonNull(runnable, "`runnable` **MUST** not be `null`");
    Objects.requireNonNull(period, "`period` **MUST** not be `null`");
    Objects.requireNonNull(unit, "`unit` **MUST** not be `null`");
    Objects.requireNonNull(delay, "`delay` **MUST** not be `null`");

    this.scheduledExecutorService.scheduleAtFixedRate(runnable, delay ? period : 0, period, unit);
  }

  @Override
  public void stop() {
    this.scheduledExecutorService.shutdown();
  }

}
