package com.mechjacktv.typedobject;

public class TypedStringUnitTests extends StronglyTypedContractTests<String> {

  @Override
  protected String givenIHaveAValue() {
    return "TEST STRING";
  }

  @Override
  protected StronglyTyped givenIHaveAStronglyTypedValue(final String value) {
    return new TestTypedString(value);
  }

  private static final class TestTypedString extends TypedString {

    private TestTypedString(final String value) {
      super(value);
    }

  }
}
