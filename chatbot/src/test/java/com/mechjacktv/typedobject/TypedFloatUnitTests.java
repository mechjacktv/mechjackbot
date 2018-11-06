package com.mechjacktv.typedobject;

public class TypedFloatUnitTests extends StronglyTypedContractTests<Float> {

  @Override
  protected Float givenIHaveAValue() {
    return Float.MAX_VALUE;
  }

  @Override
  protected StronglyTyped<Float> givenIHaveAStronglyTypedValue(final Float value) {
    return new TestTypedFloat(value);
  }

  private static final class TestTypedFloat extends TypedFloat {

    private TestTypedFloat(final Float value) {
      super(value);
    }

  }
}
