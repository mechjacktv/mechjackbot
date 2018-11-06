package com.mechjacktv.typedobject;

public class TypedShortUnitTests extends StronglyTypedContractTests<Short> {

  @Override
  protected Short givenIHaveAValue() {
    return Short.MAX_VALUE;
  }

  @Override
  protected StronglyTyped<Short> givenIHaveAStronglyTypedValue(final Short value) {
    return new TestTypedShort(value);
  }

  private static final class TestTypedShort extends TypedShort {

    private TestTypedShort(final Short value) {
      super(value);
    }

  }
}
