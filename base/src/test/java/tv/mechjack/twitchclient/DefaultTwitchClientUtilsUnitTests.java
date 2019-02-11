package tv.mechjack.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slf4j.Logger;

import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.InvocationCounter;

public class DefaultTwitchClientUtilsUnitTests extends TwitchClientUtilsContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

  @Override
  protected TwitchClientUtils givenASubjectToTest() {
    return this.givenASubjectToTest(this.testFrameworkRule.fakeFactory().fake(Logger.class));
  }

  private DefaultTwitchClientUtils givenASubjectToTest(final Logger logger) {
    return new DefaultTwitchClientUtils(this.testFrameworkRule.getInstance(TwitchClientConfiguration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(UrlConnectionFactory.class), logger);
  }

  @Test
  public final void handleUnknownObjectName_whenCalled_resultIsWarningLogged() {
    this.installModules();
    final FakeBuilder<Logger> fakeBuilder = this.testFrameworkRule.fakeBuilder(Logger.class);
    final InvocationCounter countingHandler = new InvocationCounter();
    fakeBuilder.forMethod("warn", new Class[] { String.class }).addHandler(countingHandler);
    final DefaultTwitchClientUtils subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    subjectUnderTest.handleUnknownObjectName(this.testFrameworkRule.arbitraryData().getString());

    assertThat(countingHandler.getCallCount()).isOne();
  }

}
