package tv.mechjack.testframework;

public final class DefaultTestClock implements TestClock {

  private Long currentTime = 0L;

  DefaultTestClock() {
    // making constructor package private
    super();
  }

  @Override
  public final Long currentTime() {
    return this.currentTime;
  }

  @Override
  public final void currentTimeDelta(final long timeDelta) {
    this.currentTime += timeDelta;
  }

  @Override
  public void reset() {
    this.currentTime = 0L;
  }

}
