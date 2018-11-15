package com.mechjacktv.mechjackbot.configuration;

import static com.mechjacktv.mechjackbot.configuration.PropertiesChatBotConfiguration.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.mechjacktv.test.ArbitraryDataGenerator;
import com.mechjacktv.util.HotUpdatePropertiesWrapperContractTests;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesChatBotConfiguration_HotUpdatePropertiesWrapperUnitTests
    extends HotUpdatePropertiesWrapperContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected PropertiesChatBotConfiguration givenASubjectToTest(final Supplier<InputStream> propertiesSupplier,
      final ScheduleService scheduleService) {
    return new PropertiesChatBotConfiguration(this.arbitraryDataGenerator.getString(), propertiesSupplier,
        scheduleService);
  }

  @Override
  protected Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    properties.put(TWITCH_CHANNEL_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_CLIENT_ID_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_PASSWORD_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_USERNAME_KEY, this.arbitraryDataGenerator.getString());
    return properties;
  }

}
