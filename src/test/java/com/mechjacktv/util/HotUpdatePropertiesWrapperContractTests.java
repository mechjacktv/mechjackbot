package com.mechjacktv.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.slf4j.Logger;

import com.mechjacktv.util.scheduleservice.ScheduleService;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public abstract class HotUpdatePropertiesWrapperContractTests {

  private static final String KEY_1 = "KEY_1";
  private static final String KEY_2 = "KEY_2";
  private static final String VALUE_1 = "VALUE_1";
  private static final String VALUE_2 = "VALUE_2";

  private HotUpdatePropertiesWrapper givenASubjectToTest(final Supplier<InputStream> propertiesSupplier) {
    return this.givenASubjectToTest(propertiesSupplier, mock(ScheduleService.class), mock(Logger.class));
  }

  protected abstract HotUpdatePropertiesWrapper givenASubjectToTest(Supplier<InputStream> propertiesSupplier,
      ScheduleService scheduleService, Logger logger);

  private InputStream givenAPropertiesInputStream() {
    return new ByteArrayInputStream((String.format("%s = %s\n", KEY_1, VALUE_1)
        + String.format("%s = %s\n", KEY_2, VALUE_2)).getBytes());
  }

  @Test
  public final void getProperties_withPropertiesInputStream_returnsLoadedProperties() {
    final InputStream properitesInputStream = this.givenAPropertiesInputStream();
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(() -> properitesInputStream);

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
  public final void getProperties_withExceptionGettingInputStream_logsError() {
    final Logger log = mock(Logger.class);
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(() -> {
      throw new IORuntimeException("test exception", new Exception());
    }, mock(ScheduleService.class), log);

    subjectUnderTest.getProperties();

    verify(log).error(isA(String.class), isA(Throwable.class));
  }

}
