package com.mechjacktv.configuration;

import static com.mechjacktv.testframework.TestFrameworkRule.ARBITRARY_COLLECTION_SIZE;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.HotUpdatePropertiesWrapperContractTests;
import com.mechjacktv.util.PropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesConfiguration_HotUpdatePropertiesWrapperUnitTests
    extends HotUpdatePropertiesWrapperContractTests {

  @Override
  protected PropertiesConfiguration givenASubjectToTest(final PropertiesSource propertiesSource) {
    return new PropertiesConfiguration(propertiesSource, this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(ScheduleService.class));
  }

  @Override
  protected Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      properties.put(this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString());
    }
    return properties;
  }

}
