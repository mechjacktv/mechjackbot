package tv.mechjack.testframework;

import java.util.Optional;

import javax.inject.Inject;

import org.assertj.core.api.SoftAssertions;

public final class AssertionUtils {

  private final Optional<NullMessageForNameFactory> nullMessageForNameFactory;

  @Inject
  AssertionUtils(final Optional<NullMessageForNameFactory> nullMessageForNameFactory) {
    this.nullMessageForNameFactory = nullMessageForNameFactory;
  }

  public final void assertNullPointerException(final Throwable thrown, final String name) {
    final SoftAssertions softly = new SoftAssertions();

    softly.assertThat(thrown).isInstanceOf(NullPointerException.class);
    this.nullMessageForNameFactory.ifPresent(factory -> {
      softly.assertThat(thrown).hasMessage(factory.create(name));
    });
    softly.assertAll();
  }

}
