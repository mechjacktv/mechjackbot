package tv.mechjack.platform.utils;

public class DefaultExecutionUtilsUnitTests extends ExecutionUtilsContractTests {

  @Override
  ExecutionUtils givenASubjectToTest() {
    return new DefaultExecutionUtils();
  }

}
