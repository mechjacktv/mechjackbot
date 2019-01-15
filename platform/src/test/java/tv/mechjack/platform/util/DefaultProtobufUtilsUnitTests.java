package tv.mechjack.platform.util;

public class DefaultProtobufUtilsUnitTests extends ProtobufUtilsContractTests {

  @Override
  protected DefaultProtobufUtils givenASubjectToTest() {
    return new DefaultProtobufUtils(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

  @Override
  protected void installModules() {
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

}
