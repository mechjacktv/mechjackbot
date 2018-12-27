package tv.mechjack.util.typedobject;

public class TypedStringUnitTests extends StronglyTypedContractTests<String> {

  @Override
  protected String givenIHaveAValue() {
    return "TEST STRING";
  }

  @Override
  protected String givenIHaveADifferentValue() {
    return "DIFFERENT TEST STRING";
  }

  @Override
  protected StronglyTyped<String> givenIHaveAStronglyTypedValue(final String value) {
    return TestTypedString.of(value);
  }

  private static final class TestTypedString extends TypedString {

    public static TestTypedString of(final String value) {
      return TypedString.of(TestTypedString.class, value);
    }

    private TestTypedString(final String value) {
      super(value);
    }

  }
}
