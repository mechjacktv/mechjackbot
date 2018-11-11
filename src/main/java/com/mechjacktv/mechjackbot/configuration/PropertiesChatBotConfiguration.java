package com.mechjacktv.mechjackbot.configuration;

import java.io.File;

import javax.inject.Inject;

import com.google.common.base.Strings;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;
import com.mechjacktv.twitchclient.TwitchClientId;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.FileInputStreamSupplier;
import com.mechjacktv.util.HotUpdatePropertiesWrapper;
import com.mechjacktv.util.scheduleservice.ScheduleService;

final class PropertiesChatBotConfiguration extends HotUpdatePropertiesWrapper
    implements ChatBotConfiguration, TwitchClientConfiguration {

  private static final String DATA_LOCATION_KEY = "mechjackbot.data_location";
  private static final String DATA_LOCATION_DEFAULT = System.getProperty("user.home") + "/.mechjackbot";
  private static final String DATA_LOCATION = System.getProperty(DATA_LOCATION_KEY, DATA_LOCATION_DEFAULT);
  private static final String CONFIG_PROPERTIES_FILE_NAME = "chat_bot.config";

  private static final String TWITCH_CHANNEL_KEY = "twitch.channel";
  private static final String TWITCH_CLIENT_ID_KEY = "twitch.client_id";
  private static final String TWITCH_PASSWORD_KEY = "twitch.password";
  private static final String TWITCH_USERNAME_KEY = "twitch.username";

  private final DataLocation dataLocation;

  @Inject
  public PropertiesChatBotConfiguration(final ExecutionUtils executionUtils, final ScheduleService scheduleService) {
    super(new FileInputStreamSupplier(executionUtils, new File(DATA_LOCATION, CONFIG_PROPERTIES_FILE_NAME)),
        scheduleService);

    if (this.isMissingRequiredValues()) {
      throw new IllegalStateException(String.format("Please complete your chat bot configuration (%s)",
          new File(new File(DATA_LOCATION), CONFIG_PROPERTIES_FILE_NAME).getPath()));
    }
    this.dataLocation = DataLocation.of(DATA_LOCATION);
  }

  private boolean isMissingRequiredValues() {
    return Strings.isNullOrEmpty(this.getProperties().getProperty(TWITCH_USERNAME_KEY))
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
  public TwitchUsername getTwitchUsername() {
    return TwitchUsername.of(this.getProperties().getProperty(TWITCH_USERNAME_KEY));
  }

}
