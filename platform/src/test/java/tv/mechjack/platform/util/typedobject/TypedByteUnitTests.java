package tv.mechjack.platform.util.typedobject;

public class TypedByteUnitTests extends StronglyTypedContractTests<Byte> {

  @Override
  protected Byte givenIHaveAValue() {
    return Byte.MAX_VALUE;
  }

  @Override
  protected Byte givenIHaveADifferentValue() {
    return Byte.MIN_VALUE;
  }

  @Override
  protected StronglyTyped<Byte> givenIHaveAStronglyTypedValue(final Byte value) {
    return TestTypedByte.of(value);
  }

  private static final class TestTypedByte extends TypedByte {

    public static TestTypedByte of(final Byte value) {
      return of(TestTypedByte.class, value);
    }

    private TestTypedByte(final Byte value) {
      super(value);
    }

  }
}
