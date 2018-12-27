package tv.mechjack.configuration;

import java.util.HashMap;
import java.util.Map;

import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.HotUpdatePropertiesWrapperContractTests;
import tv.mechjack.util.PropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

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

    for (int i = 0; i < TestFrameworkRule.ARBITRARY_COLLECTION_SIZE; i++) {
      properties.put(this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString());
    }
    return properties;
  }

}
