package tv.mechjack.util;

public class DefaultTimeUtilsUnitTests extends TimeUtilsContractTests {

  @Override
  TimeUtils givenASubjectToTest() {
    return new DefaultTimeUtils();
  }

}
