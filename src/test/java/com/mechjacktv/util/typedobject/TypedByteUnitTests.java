package com.mechjacktv.util.typedobject;

public class TypedByteUnitTests extends StronglyTypedContractTests<Byte> {

  @Override
  protected Byte givenIHaveAValue() {
    return Byte.MAX_VALUE;
  }

  @Override
  protected Byte givenIHaveADifferentValue() {
    return Byte.MIN_VALUE;
  }

  @Override
  protected StronglyTyped<Byte> givenIHaveAStronglyTypedValue(final Byte value) {
    return new TestTypedByte(value);
  }

  private static final class TestTypedByte extends TypedByte {

    private TestTypedByte(final Byte value) {
      super(value);
    }

  }
}
