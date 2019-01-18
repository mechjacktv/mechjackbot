package tv.mechjack.platform.utils;

import javax.inject.Inject;

import tv.mechjack.testframework.TestRandom;

public class TestRandomUtils implements RandomUtils {

  private final TestRandom testRandom;

  @Inject
  TestRandomUtils(final TestRandom testRandom) {
    this.testRandom = testRandom;
  }

  @Override
  public Integer nextInteger() {
    return testRandom.nextInteger();
  }

  @Override
  public Integer nextInteger(final Integer bound) {
    return testRandom.nextInteger();
  }

}
