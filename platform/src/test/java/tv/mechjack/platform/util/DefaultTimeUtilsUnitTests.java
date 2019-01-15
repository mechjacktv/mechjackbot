package tv.mechjack.platform.util;

public class DefaultTimeUtilsUnitTests extends TimeUtilsContractTests {

  @Override
  TimeUtils givenASubjectToTest() {
    return new DefaultTimeUtils();
  }

}
