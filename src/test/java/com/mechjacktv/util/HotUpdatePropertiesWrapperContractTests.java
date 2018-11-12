package com.mechjacktv.util;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.util.scheduleservice.ScheduleService;

public abstract class HotUpdatePropertiesWrapperContractTests {

  protected static final String KEY_1 = "KEY_1";
  protected static final String KEY_2 = "KEY_2";
  protected static final String VALUE_1 = "VALUE_1";
  protected static final String VALUE_2 = "VALUE_2";

  private HotUpdatePropertiesWrapper givenASubjectToTest(final Supplier<InputStream> propertiesSupplier) {
    return this.givenASubjectToTest(propertiesSupplier, mock(ScheduleService.class));
  }

  @SuppressWarnings("unchecked")
  private void givenASubjectToTest(final ScheduleService scheduleService) {
    final Supplier<InputStream> supplier = mock(Supplier.class);

    when(supplier.get()).thenReturn(mock(InputStream.class));
    this.givenASubjectToTest(supplier, scheduleService);
  }

  protected abstract HotUpdatePropertiesWrapper givenASubjectToTest(Supplier<InputStream> propertiesSupplier,
      ScheduleService scheduleService);

  protected InputStream givenAPropertiesInputStream() {
    return new ByteArrayInputStream((String.format("%s = %s\n", KEY_1, VALUE_1)
        + String.format("%s = %s\n", KEY_2, VALUE_2)).getBytes());
  }

  @Test
  public final void getProperties_withPropertiesInputStream_returnsLoadedProperties() {
    final InputStream propertiesInputStream = this.givenAPropertiesInputStream();
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(() -> propertiesInputStream);

    final Properties result = subjectUnderTest.getProperties();

    final SoftAssertions softly = new SoftAssertions();

    softly.assertThat(result).hasSize(2);
    softly.assertThat(result.containsKey(KEY_1)).isTrue();
    softly.assertThat(result.containsValue(VALUE_1)).isTrue();
    softly.assertThat(result.containsKey(KEY_2)).isTrue();
    softly.assertThat(result.containsValue(VALUE_2)).isTrue();
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
