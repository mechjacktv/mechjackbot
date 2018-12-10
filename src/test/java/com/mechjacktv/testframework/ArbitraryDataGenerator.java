package com.mechjacktv.testframework;

import java.util.concurrent.atomic.AtomicLong;

public final class ArbitraryDataGenerator {

  private final AtomicLong atomicLong;

  public ArbitraryDataGenerator() {
    this.atomicLong = new AtomicLong();
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
