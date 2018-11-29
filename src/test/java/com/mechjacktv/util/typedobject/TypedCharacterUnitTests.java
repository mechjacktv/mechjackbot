package com.mechjacktv.util.typedobject;

public class TypedCharacterUnitTests extends StronglyTypedContractTests<Character> {

  @Override
  protected Character givenIHaveAValue() {
    return Character.MAX_VALUE;
  }

  @Override
  protected Character givenIHaveADifferentValue() {
    return Character.MIN_VALUE;
  }

  @Override
  protected StronglyTyped<Character> givenIHaveAStronglyTypedValue(final Character value) {
    return TestTypedCharacter.of(value);
  }

  private static final class TestTypedCharacter extends TypedCharacter {

    public static TestTypedCharacter of(final Character value) {
      return TypedCharacter.of(TestTypedCharacter.class, value);
    }

    private TestTypedCharacter(final Character value) {
      super(value);
    }

  }
}
