package com.mechjacktv.util;

import java.util.concurrent.atomic.AtomicLong;

import com.mechjacktv.proto.util.UtilsMessage.TestMessage;

public final class ArbitraryDataGenerator {

  private final AtomicLong atomicLong;

  public ArbitraryDataGenerator() {
    this.atomicLong = new AtomicLong();
  }

  public final byte[] getByteArray() {
    return this.getString().getBytes();
  }

  public final long getLong() {
    return this.atomicLong.getAndIncrement();
  }

  public final String getString() {
    return String.format("Arbitrary-%d", this.getLong());
  }

  public final TestMessage getTestMessage() {
    return TestMessage.newBuilder().setValue(this.getString()).build();
  }

}
