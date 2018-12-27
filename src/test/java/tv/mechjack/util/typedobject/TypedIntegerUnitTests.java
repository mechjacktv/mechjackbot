package tv.mechjack.util.typedobject;

public class TypedIntegerUnitTests extends StronglyTypedContractTests<Integer> {

  @Override
  protected Integer givenIHaveAValue() {
    return Integer.MAX_VALUE;
  }

  @Override
  protected Integer givenIHaveADifferentValue() {
    return Integer.MIN_VALUE;
  }

  @Override
  protected StronglyTyped<Integer> givenIHaveAStronglyTypedValue(final Integer value) {
    return TestTypedInteger.of(value);
  }

  private static final class TestTypedInteger extends TypedInteger {

    public static TestTypedInteger of(final Integer value) {
      return TypedInteger.of(TestTypedInteger.class, value);
    }

    private TestTypedInteger(final Integer value) {
      super(value);
    }

  }
}
