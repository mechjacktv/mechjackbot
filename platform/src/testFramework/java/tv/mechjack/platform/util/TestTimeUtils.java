package tv.mechjack.platform.util;

import javax.inject.Inject;

import tv.mechjack.testframework.TestClock;

public class TestTimeUtils implements TimeUtils {

  private final DefaultTimeUtils defaultTimeUtils;
  private final TestClock testClock;

  @Inject
  TestTimeUtils(final TestClock testClock) {
    this.defaultTimeUtils = new DefaultTimeUtils();
    this.testClock = testClock;
  }

  @Override
  public Long currentTime() {
    return this.testClock.currentTime();
  }

  @Override
  public Long hoursAsMs(final Integer hours) {
    return this.defaultTimeUtils.hoursAsMs(hours);
  }

  @Override
  public Integer secondsAsMs(final Integer seconds) {
    return this.defaultTimeUtils.secondsAsMs(seconds);
  }

}
