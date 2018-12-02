package com.mechjacktv.util;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;

import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.util.function.ConsumerWithException;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class HotUpdatePropertiesWrapperUnitTests extends HotUpdatePropertiesWrapperContractTests {

  private static final Integer NUMBER_OF_PROPERTIES = 3;

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected HotUpdatePropertiesWrapper givenASubjectToTest(final PropertiesSource propertiesSource,
      final ScheduleService scheduleService) {
    return this.givenASubjectToTest(propertiesSource, scheduleService, mock(Logger.class));
  }

  private HotUpdatePropertiesWrapper givenASubjectToTest(final PropertiesSource propertiesSource,
      final Logger logger) {
    return this.givenASubjectToTest(propertiesSource, mock(ScheduleService.class), logger);
  }

  private HotUpdatePropertiesWrapper givenASubjectToTest(final PropertiesSource propertiesSource,
      final ScheduleService scheduleService, final Logger logger) {
    return new TestHotUpdatePropertiesWrapper(propertiesSource, scheduleService, logger);
  }

  @Override
  protected Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    for (int i = 0; i < NUMBER_OF_PROPERTIES; i++) {
      properties.put(this.arbitraryDataGenerator.getString(), this.arbitraryDataGenerator.getString());
    }
    return properties;
  }

  @Test
  public final void getProperties_withExceptionReadingPropertiesSource_logsError() throws Exception {
    final Logger logger = mock(Logger.class);
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(new ExceptionalPropertiesSource(),
        logger);

    subjectUnderTest.getProperties();

    verify(logger).error(isA(String.class), isA(Throwable.class));
  }

  private static final class TestHotUpdatePropertiesWrapper extends HotUpdatePropertiesWrapper {

    TestHotUpdatePropertiesWrapper(final PropertiesSource propertiesSource, final ScheduleService scheduleService,
        final Logger logger) {
      super(propertiesSource, scheduleService, logger);
    }

  }

  private static final class ExceptionalPropertiesSource implements PropertiesSource {

    @Override
    public void read(final ConsumerWithException<InputStream> propertiesLoader) throws Exception {
      throw new Exception();
    }
  }

}
