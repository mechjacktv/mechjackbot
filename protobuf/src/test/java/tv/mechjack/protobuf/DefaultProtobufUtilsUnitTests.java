package tv.mechjack.protobuf;

import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.TestUtilsModule;

public class DefaultProtobufUtilsUnitTests extends ProtobufUtilsContractTests {

  @Override
  protected DefaultProtobufUtils givenASubjectToTest() {
    return new DefaultProtobufUtils(this.testFrameworkRule.getInstance(
        ExecutionUtils.class));
  }

  @Override
  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestUtilsModule());
    this.testFrameworkRule.registerModule(new TestProtobufModule());
  }

}
