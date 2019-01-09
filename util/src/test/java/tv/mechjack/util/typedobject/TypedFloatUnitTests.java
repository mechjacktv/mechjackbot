package tv.mechjack.util.typedobject;

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
    return TestTypedFloat.of(value);
  }

  private static final class TestTypedFloat extends TypedFloat {

    public static TestTypedFloat of(final Float value) {
      return of(TestTypedFloat.class, value);
    }

    private TestTypedFloat(final Float value) {
      super(value);
    }

  }
}
