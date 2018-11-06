package com.mechjacktv.typedobject;

public class TypedDoubleUnitTests extends StronglyTypedContractTests<Double> {

  @Override
  protected Double givenIHaveAValue() {
    return Double.MAX_VALUE;
  }

  @Override
  protected StronglyTyped givenIHaveAStronglyTypedValue(final Double value) {
    return new TestTypedDouble(value);
  }

  private static final class TestTypedDouble extends TypedDouble {

    private TestTypedDouble(final Double value) {
      super(value);
    }

  }
}
