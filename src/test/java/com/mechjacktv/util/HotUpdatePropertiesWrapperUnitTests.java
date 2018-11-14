package com.mechjacktv.util;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.Test;
import org.slf4j.Logger;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class HotUpdatePropertiesWrapperUnitTests extends HotUpdatePropertiesWrapperContractTests {

  private static final Integer NUMBER_OF_PROPERTIES = 3;

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected HotUpdatePropertiesWrapper givenASubjectToTest(final Supplier<InputStream> propertiesSupplier,
      final ScheduleService scheduleService) {
    return this.givenASubjectToTest(propertiesSupplier, scheduleService, mock(Logger.class));
  }

  private HotUpdatePropertiesWrapper givenASubjectToTest(final Supplier<InputStream> propertiesSupplier,
      final ScheduleService scheduleService, final Logger logger) {
    return new TestHotUpdatePropertiesWrapper(propertiesSupplier, scheduleService, logger);
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
  public final void getProperties_withExceptionGettingInputStream_logsError() {
    final Logger log = mock(Logger.class);
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(() -> {
      throw new IORuntimeException("test exception", new Exception());
    }, mock(ScheduleService.class), log);

    subjectUnderTest.getProperties();

    verify(log).error(isA(String.class), isA(Throwable.class));
  }

  private static final class TestHotUpdatePropertiesWrapper extends HotUpdatePropertiesWrapper {

    TestHotUpdatePropertiesWrapper(final Supplier<InputStream> propertiesSupplier,
        final ScheduleService scheduleService, final Logger logger) {
      super(propertiesSupplier, scheduleService, logger);
    }

  }

}
