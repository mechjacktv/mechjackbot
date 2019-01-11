package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.twitchclient.TwitchLogin;

public abstract class ChatBotConfigurationContractTests {

  public static final String TWITCH_CHANNEL_KEY = "TWITCH_CHANNEL_KEY";
  public static final String TWITCH_PASSWORD_KEY = "TWITCH_PASSWORD_KEY";
  public static final String TWITCH_LOGIN_KEY = "TWITCH_LOGIN_KEY";

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    /* no-op (2019-01-06 mechjack) */
  }

  protected abstract ChatBotConfiguration givenASubjectToTest(Map<String, String> properties);

  private Map<String, String> givenIHaveAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    properties.put(TWITCH_CHANNEL_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(TWITCH_PASSWORD_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(TWITCH_LOGIN_KEY, this.testFrameworkRule.getArbitraryString());
    return properties;
  }

  @Test
  public final void new_channelMissing_throwsIllegalStateException() {
    this.installModules();
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_CHANNEL_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void new_passwordMissing_throwsIllegalStateException() {
    this.installModules();
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_PASSWORD_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void new_usernameKeyMissing_throwsIllegalStateException() {
    this.installModules();
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_LOGIN_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void getTwitchChannel_isPresent_returnsTwitchChannel() {
    this.installModules();
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final ChatChannel result = subjectUnderTest.getChatChannel();

    assertThat(result).isEqualTo(ChatChannel.of(properties.get(TWITCH_CHANNEL_KEY)));
  }

  @Test
  public final void getTwitchPassword_isPresent_returnsTwitchPassword() {
    this.installModules();
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final UserPassword result = subjectUnderTest.getUserPassword();

    assertThat(result).isEqualTo(UserPassword.of(properties.get(TWITCH_PASSWORD_KEY)));
  }

  @Test
  public final void getTwitchLogin_isPresent_returnsTwitchLogin() {
    this.installModules();
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final TwitchLogin result = subjectUnderTest.getTwitchLogin();

    assertThat(result).isEqualTo(TwitchLogin.of(properties.get(TWITCH_LOGIN_KEY)));
  }

}
