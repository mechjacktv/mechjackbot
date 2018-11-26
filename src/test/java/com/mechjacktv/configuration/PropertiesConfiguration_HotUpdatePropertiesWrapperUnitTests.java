package com.mechjacktv.configuration;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.HotUpdatePropertiesWrapperContractTests;
import com.mechjacktv.util.PropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesConfiguration_HotUpdatePropertiesWrapperUnitTests
    extends HotUpdatePropertiesWrapperContractTests {

  private static final Integer NUMBER_OF_PROPERTIES = 3;

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected PropertiesConfiguration givenASubjectToTest(final PropertiesSource propertiesSource,
      final ScheduleService scheduleService) {
    return new PropertiesConfiguration(propertiesSource, new DefaultExecutionUtils(), scheduleService);
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
