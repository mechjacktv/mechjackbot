package com.mechjacktv.mechjackbot.configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.HotUpdatePropertiesWrapperContractTests;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesAppConfiguration_HotUpdatePropertiesWrapperUnitTests
    extends HotUpdatePropertiesWrapperContractTests {

  private static final Integer NUMBER_OF_PROPERTIES = 3;

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected PropertiesAppConfiguration givenASubjectToTest(final Supplier<InputStream> propertiesSupplier,
      final ScheduleService scheduleService) {
    return new PropertiesAppConfiguration(propertiesSupplier, new DefaultExecutionUtils(), scheduleService);
  }

  @Override
  protected Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    for (int i = 0; i < NUMBER_OF_PROPERTIES; i++) {
      properties.put(this.arbitraryDataGenerator.getString(), this.arbitraryDataGenerator.getString());
    }
    return properties;
  }

}
