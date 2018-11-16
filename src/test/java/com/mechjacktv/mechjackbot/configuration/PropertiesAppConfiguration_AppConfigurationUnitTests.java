package com.mechjacktv.mechjackbot.configuration;

import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.AppConfigurationContractTests;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.PropertiesUtils;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesAppConfiguration_AppConfigurationUnitTests extends AppConfigurationContractTests {

  private final PropertiesUtils propertiesUtils = new PropertiesUtils();

  @Override
  protected AppConfiguration givenASubjectToTest(final Map<String, String> properties) {
    return this.givenASubjectToTest(() -> this.propertiesUtils.propertiesMapAsInputStream(properties));
  }

  private PropertiesAppConfiguration givenASubjectToTest(final Supplier<InputStream> propertiesSupplier) {
    return new PropertiesAppConfiguration(propertiesSupplier, new DefaultExecutionUtils(),
        mock(ScheduleService.class));
  }

}
