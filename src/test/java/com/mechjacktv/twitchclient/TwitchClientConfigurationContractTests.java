package com.mechjacktv.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Optional;

import org.junit.Test;

public abstract class TwitchClientConfigurationContractTests {

  private static final String TWITCH_CLIENT_ID = "TWITCH_CLIENT_ID";

  protected abstract TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId);

  @Test
  public final void new_clientIdMissing_throwsIllegalStateException() {
    // no assembly

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(Optional.empty()));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void getTwitchClientId_isPresent_returnsTwitchClientId() {
    final TwitchClientConfiguration subjectUnderTest = this.givenASubjectToTest(Optional.of(TWITCH_CLIENT_ID));

    final TwitchClientId result = subjectUnderTest.getTwitchClientId();

    assertThat(result.value).isEqualTo(TWITCH_CLIENT_ID);
  }

}
