package tv.mechjack.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFramework;

public abstract class TwitchClientConfigurationContractTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  protected void installModules() {
    /* no-op (2019-01-06 mechjack) */
  }

  protected abstract TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId);

  @Test
  public final void new_clientIdMissing_throwsIllegalStateException() {
    this.installModules();

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(Optional.empty()));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void getTwitchClientId_isPresent_returnsTwitchClientId() {
    this.installModules();
    final String clientId = this.testFrameworkRule.arbitraryData().getString();
    final TwitchClientConfiguration subjectUnderTest = this.givenASubjectToTest(Optional.of(clientId));

    final TwitchClientId result = subjectUnderTest.getTwitchClientId();

    assertThat(result.value).isEqualTo(clientId);
  }

}
