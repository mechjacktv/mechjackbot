package com.mechjacktv.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.mechjacktv.util.scheduleservice.ScheduleService;

public abstract class HotUpdatePropertiesWrapperContractTests {

  protected HotUpdatePropertiesWrapper givenASubjectToTest(final PropertiesSource propertiesSource) {
    return this.givenASubjectToTest(propertiesSource, mock(ScheduleService.class));
  }

  private void givenASubjectToTest(final ScheduleService scheduleService) {
    this.givenASubjectToTest(new MapPropertiesSource(this.givenAPropertiesMap()), scheduleService);
  }

  protected abstract HotUpdatePropertiesWrapper givenASubjectToTest(final PropertiesSource propertiesSource,
      ScheduleService scheduleService);

  protected abstract Map<String, String> givenAPropertiesMap();

  @Test
  public final void getProperties_withPropertiesSource_returnsLoadedProperties() {
    final Map<String, String> properties = this.givenAPropertiesMap();
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(new MapPropertiesSource(properties));

    final Properties result = subjectUnderTest.getProperties();

    assertThat(result).containsAllEntriesOf(properties);
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
