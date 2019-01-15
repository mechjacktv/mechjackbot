package tv.mechjack.twitchclient;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.slf4j.Logger;

import tv.mechjack.platform.util.ExecutionUtils;
import tv.mechjack.platform.util.TestUtilModule;

public class DefaultTwitchClientUtilsUnitTests extends TwitchClientUtilsContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

  @Override
  protected TwitchClientUtils givenASubjectToTest() {
    return this.givenASubjectToTest(mock(Logger.class));
  }

  private DefaultTwitchClientUtils givenASubjectToTest(final Logger logger) {
    return new DefaultTwitchClientUtils(this.testFrameworkRule.getInstance(TwitchClientConfiguration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(UrlConnectionFactory.class), logger);
  }

  @Test
  public final void handleUnknownObjectName_whenCalled_resultIsWarningLogged() {
    this.installModules();
    final Logger logger = mock(Logger.class);
    final DefaultTwitchClientUtils subjectUnderTest = this.givenASubjectToTest(logger);

    subjectUnderTest.handleUnknownObjectName(this.testFrameworkRule.getArbitraryString());

    verify(logger).warn(isA(String.class));
  }

}
