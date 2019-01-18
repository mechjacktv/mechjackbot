package tv.mechjack.testframework;

public interface TestClock {

  Long currentTime();

  void currentTimeDelta(final long timeDelta);

  void reset();

}
