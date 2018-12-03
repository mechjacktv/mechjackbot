package com.mechjacktv.testframework;

public final class TestClock {

  private long currentTime = 0;

  public final long currentTime() {
    return this.currentTime;
  }

  public final void currentTimeDelta(final long timeDelta) {
    this.currentTime += timeDelta;
  }

}
