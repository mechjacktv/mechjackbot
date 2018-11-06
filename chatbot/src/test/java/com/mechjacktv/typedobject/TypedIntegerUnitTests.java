package com.mechjacktv.typedobject;

public class TypedIntegerUnitTests extends StronglyTypedContractTests<Integer> {

    @Override
    protected Integer givenIHaveAValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected StronglyTyped givenIHaveAStronglyTypedValue(final Integer value) {
        return new TestTypedInteger(value);
    }

    private static final class TestTypedInteger extends TypedInteger {

        private TestTypedInteger(final Integer value) {
            super(value);
        }

    }
}
