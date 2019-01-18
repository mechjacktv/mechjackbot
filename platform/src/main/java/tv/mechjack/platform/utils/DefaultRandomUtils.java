package tv.mechjack.platform.utils;

import java.util.Random;

public class DefaultRandomUtils implements RandomUtils {

  private static final Random RANDOM = new Random();

  @Override
  public Integer nextInteger() {
    return RANDOM.nextInt();
  }

  @Override
  public Integer nextInteger(final Integer bound) {
    return RANDOM.nextInt(bound);
  }

}
