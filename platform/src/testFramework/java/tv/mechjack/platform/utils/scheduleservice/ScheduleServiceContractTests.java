package tv.mechjack.platform.utils.scheduleservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFramework;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.ArgumentCaptor;
import tv.mechjack.testframework.fake.InvocationCounter;

public abstract class ScheduleServiceContractTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  private static final Boolean DELAY = true;
  private static final Boolean NO_DELAY = false;

  private ScheduleService givenASubjectToTest() {

    return this.givenASubjectToTest(this.testFrameworkRule.fakeFactory().fake(ScheduledExecutorService.class));
  }

  abstract ScheduleService givenASubjectToTest(ScheduledExecutorService scheduledExecutorService);

  @Test
  public final void schedule_nullRunnable_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest.schedule(null,
        this.testFrameworkRule.arbitraryData().getInteger(), TimeUnit.MINUTES));

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
        () -> subjectToTest.schedule(System::currentTimeMillis, this.testFrameworkRule.arbitraryData().getInteger(), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "unit");
  }

  @Test
  public final void schedule_noDelaySpecified_schedulesWithNoDelay() {
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = this.testFrameworkRule
        .fakeBuilder(ScheduledExecutorService.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(1);
    fakeBuilder.forMethod("scheduleAtFixedRate", new Class<?>[] { Runnable.class, long.class, long.class,
        TimeUnit.class }).setHandler(capturingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, this.testFrameworkRule.arbitraryData().getInteger(), TimeUnit.MINUTES);
    final Long result = capturingHandler.getValue();

    assertThat(result).isEqualTo(0L);
  }

  @Test
  public final void schedule_nullDelay_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest
        .schedule(System::currentTimeMillis, this.testFrameworkRule.arbitraryData().getInteger(), TimeUnit.MINUTES, null));

    this.testFrameworkRule.assertNullPointerException(thrown, "delay");
  }

  @Test
  public final void schedule_noDelay_schedulesWithNoDelay() {
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = this.testFrameworkRule
        .fakeBuilder(ScheduledExecutorService.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(1);
    fakeBuilder.forMethod("scheduleAtFixedRate", new Class<?>[] { Runnable.class, long.class, long.class,
        TimeUnit.class }).setHandler(capturingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, this.testFrameworkRule.arbitraryData().getInteger(), TimeUnit.MINUTES,
        NO_DELAY);
    final Long result = capturingHandler.getValue();

    assertThat(result).isEqualTo(0L);
  }

  @Test
  public final void schedule_withDelay_schedulesWithDelay() {
    final int period = this.testFrameworkRule.arbitraryData().getInteger();
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = this.testFrameworkRule
        .fakeBuilder(ScheduledExecutorService.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(1);
    fakeBuilder.forMethod("scheduleAtFixedRate", new Class<?>[] { Runnable.class, long.class, long.class,
        TimeUnit.class }).setHandler(capturingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, period, TimeUnit.MINUTES,
        DELAY);
    final Long result = capturingHandler.getValue();

    assertThat(result).isEqualTo(period);
  }

  @Test
  public final void stop_called_shutsDownExecutor() {
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = this.testFrameworkRule
        .fakeBuilder(ScheduledExecutorService.class);
    final InvocationCounter countingHandler = new InvocationCounter();
    fakeBuilder.forMethod("shutdown").setHandler(countingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.stop();

    assertThat(countingHandler.getCallCount()).isEqualTo(1);
  }

}
