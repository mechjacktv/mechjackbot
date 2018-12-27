package tv.mechjack.configuration;

import java.util.Map;

import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.MapPropertiesSource;
import tv.mechjack.util.PropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;
import tv.mechjack.util.scheduleservice.ScheduleServiceTestModule;

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
