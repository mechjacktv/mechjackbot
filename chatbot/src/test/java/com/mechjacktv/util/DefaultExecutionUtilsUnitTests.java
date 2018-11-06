package com.mechjacktv.util;

public class DefaultExecutionUtilsUnitTests extends ExecutionUtilsContractTests {

    @Override
    ExecutionUtils givenASubjectToTest() {
        return new DefaultExecutionUtils();
    }

}
