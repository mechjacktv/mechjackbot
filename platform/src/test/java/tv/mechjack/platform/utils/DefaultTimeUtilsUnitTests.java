package tv.mechjack.platform.utils;

public class DefaultTimeUtilsUnitTests extends TimeUtilsContractTests {

  @Override
  TimeUtils givenASubjectToTest() {
    return new DefaultTimeUtils();
  }

}
