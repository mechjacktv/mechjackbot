package com.mechjacktv.testframework;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.assertj.core.api.SoftAssertions;

public final class AssertionUtils {

  private final NullMessageForNameFactory nullMessageForNameFactory;

  @Inject
  AssertionUtils(@Nullable final NullMessageForNameFactory nullMessageForNameFactory) {
    this.nullMessageForNameFactory = nullMessageForNameFactory;
  }

  public final void assertNullPointerException(final Throwable thrown, final String name) {
    final SoftAssertions softly = new SoftAssertions();

    softly.assertThat(thrown).isInstanceOf(NullPointerException.class);
    if (Objects.nonNull(this.nullMessageForNameFactory)) {
      softly.assertThat(thrown).hasMessage(this.nullMessageForNameFactory.create(name));
    }
    softly.assertAll();
  }

}
