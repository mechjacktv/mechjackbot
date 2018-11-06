package com.mechjacktv.typedobject;

public class TypedLongUnitTests extends StronglyTypedContractTests<Long> {

  @Override
  protected Long givenIHaveAValue() {
    return Long.MAX_VALUE;
  }

  @Override
  protected StronglyTyped<Long> givenIHaveAStronglyTypedValue(final Long value) {
    return new TestTypedLong(value);
  }

  private static final class TestTypedLong extends TypedLong {

    private TestTypedLong(final Long value) {
      super(value);
    }

  }
}
