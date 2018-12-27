package tv.mechjack.twitchclient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;

public abstract class TwitchClientUtilsContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new TwitchClientTestModule());
  }

  protected abstract TwitchClientUtils givenASubjectToTest();

  @Test
  public final void handleResponse_openConnectionThrowsIOException_twitchConnectExceptionIsThrown() {
    this.installModules();
    final TestUrlConnectionFactory urlConnectionFactory = this.testFrameworkRule
        .getInstance(TestUrlConnectionFactory.class);
    urlConnectionFactory.setOpenConnectionHandler(url -> {
      throw new IOException();
    });

    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest
        .handleResponse(TwitchUrl.of(this.testFrameworkRule.getArbitraryString()),
            reader -> fail("Response handler **MUST** not be called on connection error")));

    assertThat(thrown).isInstanceOf(TwitchConnectionException.class);
  }

  @Test
  public final void handleResponse_connectionOpened_callsCorrectServiceApi() {
    this.installModules();
    final String serviceUri = this.testFrameworkRule.getArbitraryString();
    final TestUrlConnectionFactory urlConnectionFactory = this.testFrameworkRule
        .getInstance(TestUrlConnectionFactory.class);
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest();

    final String[] result = new String[1];
    urlConnectionFactory.setOpenConnectionHandler(url -> {
      result[0] = url;
      return new TestUrlConnection();
    });
    subjectUnderTest.handleResponse(TwitchUrl.of(serviceUri), reader -> {
      /* no-op (2018-12-10 mechjack) */
    });

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result[0]).contains(TwitchClientUtils.TWITCH_API_URL);
    softly.assertThat(result[0]).contains(serviceUri);
    softly.assertAll();
  }

  @Test
  public final void handleResponse_connectionOpened_setClientId() {
    this.installModules();
    final TestUrlConnectionFactory urlConnectionFactory = this.testFrameworkRule
        .getInstance(TestUrlConnectionFactory.class);
    final TestUrlConnection urlConnection = new TestUrlConnection();
    urlConnectionFactory.setOpenConnectionHandler(url -> urlConnection);
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest();

    final String[] result = new String[1];
    urlConnection.setSetRequestPropertyHandler((name, value) -> {
      if ("Client-ID".equals(name)) {
        result[0] = value;
      }
    });
    subjectUnderTest.handleResponse(TwitchUrl.of(this.testFrameworkRule.getArbitraryString()), reader -> {
      /* no-op (2018-12-10 mechjack) */
    });

    final TwitchClientConfiguration twitchClientConfiguration = this.testFrameworkRule
        .getInstance(TwitchClientConfiguration.class);
    assertThat(result[0]).isEqualTo(twitchClientConfiguration.getTwitchClientId().value);
  }

  @Test
  public final void handleResponse_getInputStreamThrowsIOException_throwsTwitchConnectException() {
    this.installModules();
    final String exceptionMessage = this.testFrameworkRule.getArbitraryString();
    final TestUrlConnectionFactory urlConnectionFactory = this.testFrameworkRule
        .getInstance(TestUrlConnectionFactory.class);
    final TestUrlConnection urlConnection = new TestUrlConnection();
    urlConnectionFactory.setOpenConnectionHandler(url -> urlConnection);
    urlConnection.setGetInputStreamHandler(() -> {
      throw new IOException(exceptionMessage);
    });
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleResponse(
        TwitchUrl.of(this.testFrameworkRule.getArbitraryString()), (reader) -> {
          /* no-op (2018-12-18 mechjack) */
        }));

    assertThat(thrown).isInstanceOf(TwitchConnectionException.class).hasMessage(exceptionMessage);
  }

  @Test
  public final void handleResponse_consumerThrowsException_throwsTwitchDataException() {
    this.installModules();
    final String exceptionMessage = this.testFrameworkRule.getArbitraryString();
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleResponse(
        TwitchUrl.of(this.testFrameworkRule.getArbitraryString()), (reader) -> {
          throw new Exception(exceptionMessage);
        }));

    assertThat(thrown).isInstanceOf(TwitchDataException.class).hasMessage(exceptionMessage);
  }

  @Test
  public final void handleResponse_allGoesWell_consumerIsCalled() {
    this.installModules();
    final TwitchClientUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean[] result = new boolean[] { false };
    subjectUnderTest.handleResponse(TwitchUrl.of(this.testFrameworkRule.getArbitraryString()),
        (inputStream) -> result[0] = true);

    assertThat(result[0]).isTrue();
  }

}
