package tv.mechjack.platform.utils;

public class DefaultProtobufUtilsUnitTests extends ProtobufUtilsContractTests {

  @Override
  protected DefaultProtobufUtils givenASubjectToTest() {
    return new DefaultProtobufUtils(this.testFrameworkRule.getInstance(ExecutionUtils.class));
  }

  @Override
  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

}
