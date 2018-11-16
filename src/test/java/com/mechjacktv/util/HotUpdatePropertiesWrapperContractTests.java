package com.mechjacktv.util;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.util.scheduleservice.ScheduleService;

public abstract class HotUpdatePropertiesWrapperContractTests {

  private final PropertiesUtils propertiesUtils = new PropertiesUtils();

  private HotUpdatePropertiesWrapper givenASubjectToTest(final Supplier<InputStream> propertiesSupplier) {
    return this.givenASubjectToTest(propertiesSupplier, mock(ScheduleService.class));
  }

  @SuppressWarnings("unchecked")
  private void givenASubjectToTest(final ScheduleService scheduleService) {
    this.givenASubjectToTest(this::givenAPropertiesInputStream, scheduleService);
  }

  protected abstract HotUpdatePropertiesWrapper givenASubjectToTest(Supplier<InputStream> propertiesSupplier,
      ScheduleService scheduleService);

  protected abstract Map<String, String> givenAPropertiesMap();

  private InputStream givenAPropertiesInputStream() {
    return this.propertiesUtils.propertiesMapAsInputStream(this.givenAPropertiesMap());
  }

  @Test
  public final void getProperties_withPropertiesInputStream_returnsLoadedProperties() {
    final Map<String, String> properties = this.givenAPropertiesMap();
    final InputStream propertiesInputStream = this.propertiesUtils.propertiesMapAsInputStream(properties);
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(() -> propertiesInputStream);

    final Properties result = subjectUnderTest.getProperties();

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).containsAllEntriesOf(properties);
    softly.assertAll();
  }

  @Test
  public final void new_withScheduleService_schedulesJob() {
    final String originalUpdatePeriod = System.getProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY);

    try {
      System.setProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY, "2");
      final ScheduleService scheduleService = mock(ScheduleService.class);

      // testing scheduling on create
      this.givenASubjectToTest(scheduleService);

      verify(scheduleService).schedule(isA(Runnable.class), eq(2), eq(TimeUnit.MINUTES), eq(true));
    } finally {
      if (originalUpdatePeriod != null) {
        System.setProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY, originalUpdatePeriod);
      } else {
        System.clearProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY);
      }
    }
  }

}
