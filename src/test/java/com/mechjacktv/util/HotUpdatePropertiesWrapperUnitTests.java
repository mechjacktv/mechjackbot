package com.mechjacktv.util;

import java.io.InputStream;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mechjacktv.util.scheduleservice.ScheduleService;

public class HotUpdatePropertiesWrapperUnitTests extends HotUpdatePropertiesWrapperContractTests {

  @Override
  protected HotUpdatePropertiesWrapper givenASubjectToTest(final Supplier<InputStream> propertiesSupplier,
      final ScheduleService scheduleService, final Logger logger) {
    return new TestHotUpdatePropertiesWrapper(propertiesSupplier, scheduleService, logger);
  }

  private static final class TestHotUpdatePropertiesWrapper extends HotUpdatePropertiesWrapper {

    public TestHotUpdatePropertiesWrapper(final Supplier<InputStream> propertiesSupplier,
        final ScheduleService scheduleService, final Logger logger) {
      super(propertiesSupplier, scheduleService, logger);
    }

  }

}
