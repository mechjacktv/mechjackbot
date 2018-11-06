package com.mechjacktv.typedobject;

public class TypedBooleanUnitTests extends StronglyTypedContractTests<Boolean> {

  @Override
  protected Boolean givenIHaveAValue() {
    return Boolean.TRUE;
  }

  @Override
  protected StronglyTyped<Boolean> givenIHaveAStronglyTypedValue(final Boolean value) {
    return new TestTypedBoolean(value);
  }

  private static final class TestTypedBoolean extends TypedBoolean {

    private TestTypedBoolean(final Boolean value) {
      super(value);
    }

  }
}
