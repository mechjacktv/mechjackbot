package com.mechjacktv.typedobject;

public class TypedCharacterUnitTests extends StronglyTypedContractTests<Character> {

  @Override
  protected Character givenIHaveAValue() {
    return Character.MAX_VALUE;
  }

  @Override
  protected StronglyTyped givenIHaveAStronglyTypedValue(final Character value) {
    return new TestTypedCharacter(value);
  }

  private static final class TestTypedCharacter extends TypedCharacter {

    private TestTypedCharacter(final Character value) {
      super(value);
    }

  }
}
