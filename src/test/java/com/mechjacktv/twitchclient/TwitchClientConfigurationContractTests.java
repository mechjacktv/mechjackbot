package com.mechjacktv.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Optional;

import org.junit.Test;

import com.mechjacktv.util.ArbitraryDataGenerator;

public abstract class TwitchClientConfigurationContractTests {

  protected final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  protected abstract TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId);

  @Test
  public final void new_clientIdMissing_throwsIllegalStateException() {
    // no assembly

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(Optional.empty()));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void getTwitchClientId_isPresent_returnsTwitchClientId() {
    final String clientId = this.arbitraryDataGenerator.getString();
    final TwitchClientConfiguration subjectUnderTest = this.givenASubjectToTest(Optional.of(clientId));

    final TwitchClientId result = subjectUnderTest.getTwitchClientId();

    assertThat(result.value).isEqualTo(clientId);
  }

}
