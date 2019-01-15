package tv.mechjack.platform.util.scheduleservice;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class TestScheduleService implements ScheduleService {

  private Boolean delay;
  private Integer period;
  private Runnable runnable;
  private boolean stopped;
  private TimeUnit unit;
  private Consumer<Runnable> runnableHandler;

  TestScheduleService() {
    this.delay = null;
    this.period = null;
    this.runnable = null;
    this.stopped = false;
    this.unit = null;
    this.runnableHandler = runnable -> {
      /* no-op (2018-12-02 mechjack) */
    };
  }

  public final Boolean getDelay() {
    return this.delay;
  }

  public final Integer getPeriod() {
    return this.period;
  }

  public final Runnable getRunnable() {
    return this.runnable;
  }

  public final TimeUnit getUnit() {
    return this.unit;
  }

  @Override
  public final void schedule(final Runnable runnable, final Integer period, final TimeUnit unit) {
    this.schedule(runnable, period, unit, false);
  }

  @Override
  public final void schedule(final Runnable runnable, final Integer period, final TimeUnit unit, final Boolean delay) {
    this.runnable = runnable;
    this.period = period;
    this.unit = unit;
    this.delay = delay;
    this.runnableHandler.accept(runnable);
  }

  public final void setRunnableHandler(final Consumer<Runnable> runnableHandler) {
    this.runnableHandler = runnableHandler;
  }

  @Override
  public final void stop() {
    this.stopped = true;
  }

  public final boolean wasStopped() {
    return this.stopped;
  }

}
