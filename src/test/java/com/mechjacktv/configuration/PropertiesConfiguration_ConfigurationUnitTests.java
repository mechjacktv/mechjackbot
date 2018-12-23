package com.mechjacktv.configuration;

import java.util.Map;

import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.MapPropertiesSource;
import com.mechjacktv.util.PropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;
import com.mechjacktv.util.scheduleservice.ScheduleServiceTestModule;

public class PropertiesConfiguration_ConfigurationUnitTests extends ConfigurationContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new ScheduleServiceTestModule());
  }

  @Override
  protected Configuration givenASubjectToTest(final Map<String, String> properties) {
    return this.givenASubjectToTest(new MapPropertiesSource(properties));
  }

  private PropertiesConfiguration givenASubjectToTest(final PropertiesSource propertiesSource) {
    return new PropertiesConfiguration(propertiesSource, this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(ScheduleService.class));
  }

}
