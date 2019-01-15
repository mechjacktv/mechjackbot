package tv.mechjack.platform.util.typedobject;

public class TypedShortUnitTests extends StronglyTypedContractTests<Short> {

  @Override
  protected Short givenIHaveAValue() {
    return Short.MAX_VALUE;
  }

  @Override
  protected Short givenIHaveADifferentValue() {
    return Short.MIN_VALUE;
  }

  @Override
  protected StronglyTyped<Short> givenIHaveAStronglyTypedValue(final Short value) {
    return TestTypedShort.of(value);
  }

  private static final class TestTypedShort extends TypedShort {

    public static TestTypedShort of(final Short value) {
      return of(TestTypedShort.class, value);
    }

    private TestTypedShort(final Short value) {
      super(value);
    }

  }
}
