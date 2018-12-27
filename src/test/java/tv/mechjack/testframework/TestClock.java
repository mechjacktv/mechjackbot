package tv.mechjack.testframework;

public final class TestClock {

  private long currentTime = 0;

  TestClock() {
    // making constructor package private
    super();
  }

  public final long currentTime() {
    return this.currentTime;
  }

  public final void currentTimeDelta(final long timeDelta) {
    this.currentTime += timeDelta;
  }

}
