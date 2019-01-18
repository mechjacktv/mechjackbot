package tv.mechjack.platform.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;

public abstract class TimeUtilsContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new TestUtilsModule());
  }

  abstract TimeUtils givenASubjectToTest();

  @Test
  public final void secondsAsMs_oneSecond_returns1000() {
    final TimeUtils subjectUnderTest = this.givenASubjectToTest();

    final Integer result = subjectUnderTest.secondsAsMs(1);

    assertThat(result).isEqualTo(TimeUtils.SECOND);
  }

  @Test
  public final void secondAsMs_nullSecond_throwsNullPointerException() {
    this.installModules();
    final TimeUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.secondsAsMs(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "seconds");
  }

  @Test
  public final void hoursAsMs_oneSecond_returns1000() {
    final TimeUtils subjectUnderTest = this.givenASubjectToTest();

    final Long result = subjectUnderTest.hoursAsMs(1);

    assertThat(result).isEqualTo(TimeUtils.HOUR);
  }

  @Test
  public final void hoursAsMs_nullSecond_throwsNullPointerException() {
    this.installModules();
    final TimeUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hoursAsMs(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "hours");
  }

}
