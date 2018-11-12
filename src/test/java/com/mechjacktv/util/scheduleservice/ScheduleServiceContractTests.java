package com.mechjacktv.util.scheduleservice;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public abstract class ScheduleServiceContractTests {

  private static final Integer PERIOD = 10;
  private static final Runnable RUNNABLE = System::currentTimeMillis;
  private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;
  private static final Boolean DELAY = true;
  private static final Boolean NO_DELAY = false;

  private ScheduleService givenASubjectToTest() {
    return this.givenASubjectToTest(mock(ScheduledExecutorService.class));
  }

  abstract ScheduleService givenASubjectToTest(ScheduledExecutorService scheduledExecutorService);

  @Test
  public final void schedule_nullRunnable_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest.schedule(null, PERIOD, TIME_UNIT));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("`runnable` **MUST** not be `null`");
  }

  @Test
  public final void schedule_nullPeriod_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest.schedule(RUNNABLE, null, TIME_UNIT));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("`period` **MUST** not be `null`");
  }

  @Test
  public final void schedule_nullTimeUnit_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest.schedule(RUNNABLE, PERIOD, null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("`unit` **MUST** not be `null`");
  }

  @Test
  public final void schedule_noDelaySpecified_schedulesWithNoDelay() {
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(RUNNABLE, PERIOD, TIME_UNIT);

    verify(scheduledExecutorService).scheduleAtFixedRate(eq(RUNNABLE), eq(0L), eq((long) PERIOD), eq(TIME_UNIT));
  }

  @Test
  public final void schedule_noDelay_schedulesWithNoDelay() {
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(RUNNABLE, PERIOD, TIME_UNIT, NO_DELAY);

    verify(scheduledExecutorService).scheduleAtFixedRate(eq(RUNNABLE), eq(0L), eq((long) PERIOD), eq(TIME_UNIT));
  }

  @Test
  public final void schedule_withDelay_schedulesWithDelay() {
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(RUNNABLE, PERIOD, TIME_UNIT, DELAY);

    verify(scheduledExecutorService).scheduleAtFixedRate(eq(RUNNABLE), eq((long) PERIOD), eq((long) PERIOD),
        eq(TIME_UNIT));
  }

  @Test
  public final void stop_called_shutsdownExecutor() {
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.stop();

    verify(scheduledExecutorService).shutdown();
  }

}
