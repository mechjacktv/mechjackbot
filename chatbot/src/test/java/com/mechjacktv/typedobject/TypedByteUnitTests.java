package com.mechjacktv.typedobject;

public class TypedByteUnitTests extends StronglyTypedContractTests<Byte> {

    @Override
    protected Byte givenIHaveAValue() {
        return Byte.MAX_VALUE;
    }

    @Override
    protected StronglyTyped givenIHaveAStronglyTypedValue(final Byte value) {
        return new TestTypedByte(value);
    }

    private static final class TestTypedByte extends TypedByte {

        private TestTypedByte(final Byte value) {
            super(value);
        }

    }
}
