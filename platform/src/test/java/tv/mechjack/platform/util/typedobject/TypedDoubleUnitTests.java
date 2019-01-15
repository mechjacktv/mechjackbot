package tv.mechjack.platform.util.typedobject;

public class TypedDoubleUnitTests extends StronglyTypedContractTests<Double> {

  @Override
  protected Double givenIHaveAValue() {
    return Double.MAX_VALUE;
  }

  @Override
  protected Double givenIHaveADifferentValue() {
    return Double.MIN_VALUE;
  }

  @Override
  protected StronglyTyped<Double> givenIHaveAStronglyTypedValue(final Double value) {
    return TestTypedDouble.of(value);
  }

  private static final class TestTypedDouble extends TypedDouble {

    public static TestTypedDouble of(final Double value) {
      return of(TestTypedDouble.class, value);
    }

    private TestTypedDouble(final Double value) {
      super(value);
    }

  }
}
