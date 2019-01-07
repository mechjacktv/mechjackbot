package tv.mechjack.configuration;

import java.util.Map;

import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.scheduleservice.HotUpdatePropertiesSource;
import tv.mechjack.util.scheduleservice.MapHotUpdatePropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;
import tv.mechjack.util.scheduleservice.TestScheduleServiceModule;

public class PropertiesConfiguration_ConfigurationUnitTests extends ConfigurationContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new TestScheduleServiceModule());
  }

  @Override
  protected Configuration givenASubjectToTest(final Map<String, String> properties) {
    return this.givenASubjectToTest(new MapHotUpdatePropertiesSource(properties));
  }

  private PropertiesConfiguration givenASubjectToTest(final HotUpdatePropertiesSource hotUpdatePropertiesSource) {
    return new PropertiesConfiguration(hotUpdatePropertiesSource,
        this.testFrameworkRule.getInstance(ExecutionUtils.class),
        this.testFrameworkRule.getInstance(ScheduleService.class));
  }

}
