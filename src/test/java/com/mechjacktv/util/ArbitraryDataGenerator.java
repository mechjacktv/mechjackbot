package com.mechjacktv.util;

import java.util.concurrent.atomic.AtomicLong;

import com.mechjacktv.proto.util.UtilsMessage.TestKeyMessage;
import com.mechjacktv.proto.util.UtilsMessage.TestValueMessage;

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

  public final TestKeyMessage getTestKeyMessage() {
    return TestKeyMessage.newBuilder().setValue(this.getString()).build();
  }

  public final TestValueMessage getTestValueMessage() {
    return TestValueMessage.newBuilder().setValue(this.getString()).build();
  }

}
