package com.mechjacktv.mechjackbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.twitchclient.TwitchLogin;

public abstract class ChatBotConfigurationContractTests {

  public static final String TWITCH_CHANNEL_KEY = "TWITCH_CHANNEL_KEY";
  public static final String TWITCH_PASSWORD_KEY = "TWITCH_PASSWORD_KEY";
  public static final String TWITCH_LOGIN_KEY = "TWITCH_LOGIN_KEY";

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private ChatBotConfiguration givenASubjectToTest(final String dataLocation) {
    return this.givenASubjectToTest(dataLocation, this.givenIHaveAPropertiesMap());
  }

  private ChatBotConfiguration givenASubjectToTest(final Map<String, String> properties) {
    return this.givenASubjectToTest(this.testFrameworkRule.getArbitraryString(), properties);
  }

  protected abstract ChatBotConfiguration givenASubjectToTest(String dataLocation, Map<String, String> properties);

  private Map<String, String> givenIHaveAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    properties.put(TWITCH_CHANNEL_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(TWITCH_PASSWORD_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(TWITCH_LOGIN_KEY, this.testFrameworkRule.getArbitraryString());
    return properties;
  }

  @Test
  public final void new_channelMissing_throwsIllegalStateException() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_CHANNEL_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void new_passwordMissing_throwsIllegalStateException() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_PASSWORD_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void new_usernameKeyMissing_throwsIllegalStateException() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_LOGIN_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void getDataLocation_isPresent_returnsDataLocation() {
    final String dataLocation = this.testFrameworkRule.getArbitraryString();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(dataLocation);

    final DataLocation result = subjectUnderTest.getDataLocation();

    assertThat(result).isEqualTo(DataLocation.of(dataLocation));
  }

  @Test
  public final void getTwitchChannel_isPresent_returnsTwitchChannel() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final TwitchChannel result = subjectUnderTest.getTwitchChannel();

    assertThat(result).isEqualTo(TwitchChannel.of(properties.get(TWITCH_CHANNEL_KEY)));
  }

  @Test
  public final void getTwitchPassword_isPresent_returnsTwitchPassword() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final TwitchPassword result = subjectUnderTest.getTwitchPassword();

    assertThat(result).isEqualTo(TwitchPassword.of(properties.get(TWITCH_PASSWORD_KEY)));
  }

  @Test
  public final void getTwitchLogin_isPresent_returnsTwitchLogin() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final TwitchLogin result = subjectUnderTest.getTwitchLogin();

    assertThat(result).isEqualTo(TwitchLogin.of(properties.get(TWITCH_LOGIN_KEY)));
  }

}
