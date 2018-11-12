package com.mechjacktv.util.typedobject;

public class TypedObjectUnitTests extends StronglyTypedContractTests<Object> {

  @Override
  protected Object givenIHaveAValue() {
    return new Object();
  }

  @Override
  protected Object givenIHaveADifferentValue() {
    return this.givenIHaveAValue();
  }

  @Override
  protected StronglyTyped<Object> givenIHaveAStronglyTypedValue(final Object value) {
    return new TestTypedObject(value);
  }

  private static final class TestTypedObject extends TypedObject {

    private TestTypedObject(final Object value) {
      super(value);
    }

  }
}
