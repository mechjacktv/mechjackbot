package tv.mechjack.testframework;

import java.util.concurrent.atomic.AtomicLong;

public final class ArbitraryDataGenerator {

  private final AtomicLong atomicLong;

  ArbitraryDataGenerator() {
    this.atomicLong = new AtomicLong(1);
  }

  public final byte[] getByteArray() {
    return this.getString().getBytes();
  }

  public final int getInteger() {
    return (int) this.getLong();
  }

  public final long getLong() {
    return this.atomicLong.getAndIncrement();
  }

  public final String getString() {
    return String.format("Arbitrary-%d", this.getLong());
  }

}
