package com.mechjacktv.util.typedobject;

public class TypedFloatUnitTests extends StronglyTypedContractTests<Float> {

  @Override
  protected Float givenIHaveAValue() {
    return Float.MAX_VALUE;
  }

  @Override
  protected Float givenIHaveADifferentValue() {
    return Float.MIN_VALUE;
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
