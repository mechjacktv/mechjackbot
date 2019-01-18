package tv.mechjack.platform.utils.scheduleservice;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;

public abstract class ScheduleServiceContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private static final Boolean DELAY = true;
  private static final Boolean NO_DELAY = false;

  private ScheduleService givenASubjectToTest() {
    return this.givenASubjectToTest(mock(ScheduledExecutorService.class));
  }

  abstract ScheduleService givenASubjectToTest(ScheduledExecutorService scheduledExecutorService);

  @Test
  public final void schedule_nullRunnable_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest.schedule(null,
        this.testFrameworkRule.getArbitraryInteger(), TimeUnit.MINUTES));

    this.testFrameworkRule.assertNullPointerException(thrown, "runnable");
  }

  @Test
  public final void schedule_nullPeriod_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectToTest.schedule(System::currentTimeMillis, null, TimeUnit.MINUTES));

    this.testFrameworkRule.assertNullPointerException(thrown, "period");
  }

  @Test
  public final void schedule_nullTimeUnit_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectToTest.schedule(System::currentTimeMillis, this.testFrameworkRule.getArbitraryInteger(), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "unit");
  }

  @Test
  public final void schedule_noDelaySpecified_schedulesWithNoDelay() {
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, this.testFrameworkRule.getArbitraryInteger(), TimeUnit.MINUTES);

    verify(scheduledExecutorService).scheduleAtFixedRate(isA(Runnable.class), eq(0L), anyLong(), eq(TimeUnit.MINUTES));
  }

  @Test
  public final void schedule_nullDelay_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest
        .schedule(System::currentTimeMillis, this.testFrameworkRule.getArbitraryInteger(), TimeUnit.MINUTES, null));

    this.testFrameworkRule.assertNullPointerException(thrown, "delay");
  }

  @Test
  public final void schedule_noDelay_schedulesWithNoDelay() {
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest
        .schedule(System::currentTimeMillis, this.testFrameworkRule.getArbitraryInteger(), TimeUnit.MINUTES, NO_DELAY);

    verify(scheduledExecutorService).scheduleAtFixedRate(isA(Runnable.class), eq(0L), anyLong(), eq(TimeUnit.MINUTES));
  }

  @Test
  public final void schedule_withDelay_schedulesWithDelay() {
    final int period = this.testFrameworkRule.getArbitraryInteger();
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, period, TimeUnit.MINUTES, DELAY);

    verify(scheduledExecutorService).scheduleAtFixedRate(isA(Runnable.class), eq((long) period), eq((long) period),
        eq(TimeUnit.MINUTES));
  }

  @Test
  public final void stop_called_shutsDownExecutor() {
    final ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.stop();

    verify(scheduledExecutorService).shutdown();
  }

}
