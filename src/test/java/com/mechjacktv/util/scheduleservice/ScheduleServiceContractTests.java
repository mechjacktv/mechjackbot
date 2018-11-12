package com.mechjacktv.util.scheduleservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public abstract class ScheduleServiceContractTests {

  private static final Integer PERIOD = 10;
  private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

  abstract ScheduleService givenASubjectToTest();

  @Test
  public final void schedule_nullRunnable_throwsNullPointerExceptionWithMessage() {
    final ScheduleService subjectToTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectToTest.schedule(null, PERIOD, TIME_UNIT));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage("`runnable` **MUST** not be `null`");
  }

}
