package com.mechjacktv.mechjackbot.chatbot;

import java.io.File;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.DataLocation;
import com.mechjacktv.mechjackbot.TwitchChannel;
import com.mechjacktv.mechjackbot.TwitchPassword;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;
import com.mechjacktv.twitchclient.TwitchClientId;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.FilePropertiesSource;
import com.mechjacktv.util.HotUpdatePropertiesWrapper;
import com.mechjacktv.util.PropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public final class PropertiesChatBotConfiguration extends HotUpdatePropertiesWrapper
    implements ChatBotConfiguration, TwitchClientConfiguration {

  private static final Logger log = LoggerFactory.getLogger(PropertiesChatBotConfiguration.class);

  private static final String DATA_LOCATION_KEY = "mechjackbot.data_location";
  private static final String DATA_LOCATION_DEFAULT = System.getProperty("user.home") + "/.mechjackbot";
  private static final String CONFIG_PROPERTIES_FILE_NAME = "chat_bot.config";

  static final String TWITCH_CHANNEL_KEY = "twitch.channel";
  static final String TWITCH_CLIENT_ID_KEY = "twitch.client_id";
  static final String TWITCH_PASSWORD_KEY = "twitch.password";
  static final String TWITCH_LOGIN_KEY = "twitch.login";

  private final DataLocation dataLocation;

  @Inject
  PropertiesChatBotConfiguration(final ScheduleService scheduleService) {
    this(System.getProperty(DATA_LOCATION_KEY, DATA_LOCATION_DEFAULT), new FilePropertiesSource(
        new File(System.getProperty(DATA_LOCATION_KEY, DATA_LOCATION_DEFAULT), CONFIG_PROPERTIES_FILE_NAME)),
        scheduleService);
  }

  PropertiesChatBotConfiguration(final String dataLocation, final PropertiesSource propertiesSource,
      final ScheduleService scheduleService) {
    super(propertiesSource, scheduleService, log);
    if (this.isMissingRequiredValues()) {
      throw new IllegalStateException(String.format("Please complete your chat bot configuration (%s)",
          new File(new File(dataLocation), CONFIG_PROPERTIES_FILE_NAME).getPath()));
    }
    this.dataLocation = DataLocation.of(dataLocation);
  }

  private boolean isMissingRequiredValues() {
    return Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_LOGIN_KEY))
        || Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_PASSWORD_KEY))
        || Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_CHANNEL_KEY))
        || Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_CLIENT_ID_KEY));
  }

  @Override
  public DataLocation getDataLocation() {
    return this.dataLocation;
  }

  @Override
  public TwitchChannel getTwitchChannel() {
    return TwitchChannel.of(this.getProperties().getProperty(TWITCH_CHANNEL_KEY));
  }

  @Override
  public TwitchClientId getTwitchClientId() {
    return TwitchClientId.of(this.getProperties().getProperty(TWITCH_CLIENT_ID_KEY));
  }

  @Override
  public TwitchPassword getTwitchPassword() {
    return TwitchPassword.of(this.getProperties().getProperty(TWITCH_PASSWORD_KEY));
  }

  @Override
  public TwitchLogin getTwitchLogin() {
    return TwitchLogin.of(this.getProperties().getProperty(TWITCH_LOGIN_KEY));
  }

}
