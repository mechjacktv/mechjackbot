package tv.mechjack.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;

public abstract class TwitchClientConfigurationContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected abstract TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId);

  @Test
  public final void new_clientIdMissing_throwsIllegalStateException() {
    // no assembly

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(Optional.empty()));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void getTwitchClientId_isPresent_returnsTwitchClientId() {
    final String clientId = this.testFrameworkRule.getArbitraryString();
    final TwitchClientConfiguration subjectUnderTest = this.givenASubjectToTest(Optional.of(clientId));

    final TwitchClientId result = subjectUnderTest.getTwitchClientId();

    assertThat(result.value).isEqualTo(clientId);
  }

}
