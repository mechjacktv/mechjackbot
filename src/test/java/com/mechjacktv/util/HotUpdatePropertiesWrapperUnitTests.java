package com.mechjacktv.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.util.function.ConsumerWithException;
import com.mechjacktv.util.scheduleservice.ScheduleService;

import org.junit.Test;
import org.slf4j.Logger;

import static com.mechjacktv.testframework.TestFrameworkRule.ARBITRARY_COLLECTION_SIZE;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HotUpdatePropertiesWrapperUnitTests extends HotUpdatePropertiesWrapperContractTests {

  @Override
  protected HotUpdatePropertiesWrapper givenASubjectToTest(final PropertiesSource propertiesSource) {
    return this.givenASubjectToTest(propertiesSource, this.testFrameworkRule.getInstance(ScheduleService.class),
        mock(Logger.class));
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

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      properties.put(this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString());
    }
    return properties;
  }

  @Test
  public final void getProperties_withExceptionReadingPropertiesSource_logsError() {
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
