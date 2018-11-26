package com.mechjacktv.configuration;

import java.util.Map;

import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.MapPropertiesSource;
import com.mechjacktv.util.PropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

import static org.mockito.Mockito.mock;

public class PropertiesAppConfiguration_ConfigurationUnitTests extends ConfigurationContractTests {

  @Override
  @SuppressWarnings("unchecked")
  protected Configuration givenASubjectToTest(final Map<String, String> properties) {
    return this.givenASubjectToTest(new MapPropertiesSource(properties));
  }

  private PropertiesConfiguration givenASubjectToTest(final PropertiesSource propertiesSource) {
    return new PropertiesConfiguration(propertiesSource, new DefaultExecutionUtils(), mock(ScheduleService.class));
  }

}
