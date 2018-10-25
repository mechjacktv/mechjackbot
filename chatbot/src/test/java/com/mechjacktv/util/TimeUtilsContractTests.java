package com.mechjacktv.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public abstract class TimeUtilsContractTests {

  abstract TimeUtils givenASubjectToTest();

  @Test
  public final void secondsAsMs_oneSecond_returns1000() {
    final TimeUtils subjectUnderTest = this.givenASubjectToTest();

    final Integer result = subjectUnderTest.secondsAsMs(1);

    assertThat(result).isEqualTo(1000);
  }

  @Test
  public final void secondAsMs_nullSecond_throwsNullPointerException() {
    final TimeUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.secondsAsMs(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessageContaining("**MUST** not be `null`");
  }

}
