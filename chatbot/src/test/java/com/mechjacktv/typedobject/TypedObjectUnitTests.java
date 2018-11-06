package com.mechjacktv.typedobject;

public class TypedObjectUnitTests extends StronglyTypedContractTests<Object> {

  @Override
  protected Object givenIHaveAValue() {
    return new Object();
  }

  @Override
  protected StronglyTyped givenIHaveAStronglyTypedValue(final Object value) {
    return new TestTypedObject(value);
  }

  private static final class TestTypedObject extends TypedObject {

    private TestTypedObject(final Object value) {
      super(value);
    }

  }
}