package tv.mechjack.platform.configuration;

import java.util.Map;

import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.scheduleservice.HotUpdatePropertiesSource;
import tv.mechjack.platform.utils.scheduleservice.MapHotUpdatePropertiesSource;
import tv.mechjack.platform.utils.scheduleservice.ScheduleService;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleServiceModule;

public class PropertiesConfiguration_ConfigurationUnitTests extends ConfigurationContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.registerModule(new TestScheduleServiceModule());
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
