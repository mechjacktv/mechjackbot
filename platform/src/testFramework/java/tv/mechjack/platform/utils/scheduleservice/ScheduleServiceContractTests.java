package tv.mechjack.platform.utils.scheduleservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.FakeFactory;
import tv.mechjack.testframework.fake.methodhandler.CountingMethodInvocationHandler;
import tv.mechjack.testframework.fake.methodhandler.ValidatingMethodInvocationHandler;

public abstract class ScheduleServiceContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private static final Boolean DELAY = true;
  private static final Boolean NO_DELAY = false;

  private ScheduleService givenASubjectToTest() {

    return this.givenASubjectToTest(this.testFrameworkRule.fake(ScheduledExecutorService.class));
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
    final FakeFactory fakeFactory = this.testFrameworkRule.getInstance(FakeFactory.class);
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = fakeFactory.builder(ScheduledExecutorService.class);
    final CountingMethodInvocationHandler countingHandler = new CountingMethodInvocationHandler();
    final ValidatingMethodInvocationHandler validatingHandler = new ValidatingMethodInvocationHandler(
        invocation -> invocation.getArgument(1).equals(0L)
            && invocation.getArgument(3).equals(TimeUnit.MINUTES),
        countingHandler);
    fakeBuilder.forMethod("scheduleAtFixedRate", new Class<?>[] { Runnable.class, long.class, long.class,
        TimeUnit.class }).addHandler(validatingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, this.testFrameworkRule.getArbitraryInteger(), TimeUnit.MINUTES);

    assertThat(countingHandler.getCallCount()).isEqualTo(1);
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
    final FakeFactory fakeFactory = this.testFrameworkRule.getInstance(FakeFactory.class);
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = fakeFactory.builder(ScheduledExecutorService.class);
    final CountingMethodInvocationHandler countingHandler = new CountingMethodInvocationHandler();
    final ValidatingMethodInvocationHandler validatingHandler = new ValidatingMethodInvocationHandler(
        invocation -> invocation.getArgument(1).equals(0L)
            && invocation.getArgument(3).equals(TimeUnit.MINUTES),
        countingHandler);
    fakeBuilder.forMethod("scheduleAtFixedRate", new Class<?>[] { Runnable.class, long.class, long.class,
        TimeUnit.class }).addHandler(validatingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, this.testFrameworkRule.getArbitraryInteger(), TimeUnit.MINUTES,
        NO_DELAY);

    assertThat(countingHandler.getCallCount()).isEqualTo(1);
  }

  @Test
  public final void schedule_withDelay_schedulesWithDelay() {
    final int period = this.testFrameworkRule.getArbitraryInteger();
    final FakeFactory fakeFactory = this.testFrameworkRule.getInstance(FakeFactory.class);
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = fakeFactory.builder(ScheduledExecutorService.class);
    final CountingMethodInvocationHandler countingHandler = new CountingMethodInvocationHandler();
    final ValidatingMethodInvocationHandler validatingHandler = new ValidatingMethodInvocationHandler(
        invocation -> invocation.getArgument(1).equals((long) period)
            && invocation.getArgument(3).equals(TimeUnit.MINUTES),
        countingHandler);
    fakeBuilder.forMethod("scheduleAtFixedRate", new Class<?>[] { Runnable.class, long.class, long.class,
        TimeUnit.class }).addHandler(validatingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.schedule(System::currentTimeMillis, period, TimeUnit.MINUTES,
        DELAY);

    assertThat(countingHandler.getCallCount()).isEqualTo(1);
  }

  @Test
  public final void stop_called_shutsDownExecutor() {
    final FakeFactory fakeFactory = this.testFrameworkRule.getInstance(FakeFactory.class);
    final FakeBuilder<ScheduledExecutorService> fakeBuilder = fakeFactory.builder(ScheduledExecutorService.class);
    final CountingMethodInvocationHandler countingHandler = new CountingMethodInvocationHandler();
    fakeBuilder.forMethod("shutdown").addHandler(countingHandler);
    final ScheduledExecutorService scheduledExecutorService = fakeBuilder.build();
    final ScheduleService subjectToTest = this.givenASubjectToTest(scheduledExecutorService);

    subjectToTest.stop();

    assertThat(countingHandler.getCallCount()).isEqualTo(1);
  }

}
