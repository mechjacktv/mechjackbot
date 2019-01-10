package tv.mechjack.util.typedobject;

public class TypedLongUnitTests extends StronglyTypedContractTests<Long> {

  @Override
  protected Long givenIHaveAValue() {
    return Long.MAX_VALUE;
  }

  @Override
  protected Long givenIHaveADifferentValue() {
    return Long.MIN_VALUE;
  }

  @Override
  protected StronglyTyped<Long> givenIHaveAStronglyTypedValue(final Long value) {
    return TestTypedLong.of(value);
  }

  private static final class TestTypedLong extends TypedLong {

    public static TestTypedLong of(final Long value) {
      return of(TestTypedLong.class, value);
    }

    private TestTypedLong(final Long value) {
      super(value);
    }

  }
}
