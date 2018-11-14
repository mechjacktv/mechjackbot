package com.mechjacktv.mechjackbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.mechjacktv.ArbitraryDataGenerator;

public abstract class ChatBotConfigurationContractTests {

  public static final String TWITCH_CHANNEL_KEY = "TWITCH_CHANNEL_KEY";
  public static final String TWITCH_PASSWORD_KEY = "TWITCH_PASSWORD_KEY";
  public static final String TWITCH_USERNAME_KEY = "TWITCH_USERNAME_KEY";

  private static final String TEST_DATA_LOCATION = "/test/data/location";

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private ChatBotConfiguration givenASubjectToTest() {
    return this.givenASubjectToTest(this.givenIHaveAPropertiesMap());
  }

  private ChatBotConfiguration givenASubjectToTest(final Map<String, String> properties) {
    return this.givenASubjectToTest(TEST_DATA_LOCATION, properties);
  }

  protected abstract ChatBotConfiguration givenASubjectToTest(String dataLocation, Map<String, String> properties);

  private Map<String, String> givenIHaveAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    properties.put(TWITCH_CHANNEL_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_PASSWORD_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_USERNAME_KEY, this.arbitraryDataGenerator.getString());
    return properties;
  }

  @Test
  public final void new_channelMissing_throwsIllegalStateException() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_CHANNEL_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(TEST_DATA_LOCATION, properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void new_passwordMissing_throwsIllegalStateException() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_PASSWORD_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(TEST_DATA_LOCATION, properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void new_usernameKeyMissing_throwsIllegalStateException() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    properties.remove(TWITCH_USERNAME_KEY);

    final Throwable thrown = catchThrowable(() -> this.givenASubjectToTest(TEST_DATA_LOCATION, properties));

    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public final void getDataLocation_isPresent_returnsDataLocation() {
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest();

    final DataLocation result = subjectUnderTest.getDataLocation();

    assertThat(result.value).isEqualTo(TEST_DATA_LOCATION);
  }

  @Test
  public final void getTwitchChannel_isPresent_returnsTwitchChannel() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final TwitchChannel result = subjectUnderTest.getTwitchChannel();

    assertThat(result.value).isEqualTo(properties.get(TWITCH_CHANNEL_KEY));
  }

  @Test
  public final void getTwitchPassword_isPresent_returnsTwitchPassword() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final TwitchPassword result = subjectUnderTest.getTwitchPassword();

    assertThat(result.value).isEqualTo(properties.get(TWITCH_PASSWORD_KEY));
  }

  @Test
  public final void getTwitchUsername_isPresent_returnsTwitchUsername() {
    final Map<String, String> properties = this.givenIHaveAPropertiesMap();
    final ChatBotConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final TwitchUsername result = subjectUnderTest.getTwitchUsername();

    assertThat(result.value).isEqualTo(properties.get(TWITCH_USERNAME_KEY));
  }

}
