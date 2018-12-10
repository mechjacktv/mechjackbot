package com.mechjacktv.util;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.scheduleservice.ScheduleServiceTestModule;
import com.mechjacktv.util.scheduleservice.TestScheduleService;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class HotUpdatePropertiesWrapperContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new ScheduleServiceTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private void constructATestSubject() {
    this.givenASubjectToTest(new MapPropertiesSource(this.givenAPropertiesMap()));
  }

  protected abstract HotUpdatePropertiesWrapper givenASubjectToTest(final PropertiesSource propertiesSource);

  protected abstract Map<String, String> givenAPropertiesMap();

  @Test
  public final void getProperties_withPropertiesSource_returnsLoadedProperties() {
    this.installModules();
    final Map<String, String> properties = this.givenAPropertiesMap();
    final HotUpdatePropertiesWrapper subjectUnderTest = this.givenASubjectToTest(new MapPropertiesSource(properties));

    final Properties result = subjectUnderTest.getProperties();

    assertThat(result).containsAllEntriesOf(properties);
  }

  @Test
  public final void new_withScheduleService_schedulesJob() {
    this.installModules();
    final int updatePeriod = 2;
    final String originalUpdatePeriod = System.getProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY);

    // TODO (2018-12-09 mechjack): Create System Properties rule
    try {
      System.setProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY, Integer.toString(updatePeriod));

      // testing scheduling on create
      this.constructATestSubject();

      final TestScheduleService scheduleService = this.testFrameworkRule.getInstance(TestScheduleService.class);
      final SoftAssertions softly = new SoftAssertions();
      softly.assertThat(scheduleService.getRunnable()).isNotNull();
      softly.assertThat(scheduleService.getPeriod()).isEqualTo(updatePeriod);
      softly.assertThat(scheduleService.getUnit()).isEqualTo(TimeUnit.MINUTES);
      softly.assertThat(scheduleService.getDelay()).isTrue();
      softly.assertAll();
    } finally {
      if (originalUpdatePeriod != null) {
        System.setProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY, originalUpdatePeriod);
      } else {
        System.clearProperty(HotUpdatePropertiesWrapper.UPDATE_PERIOD_KEY);
      }
    }
  }

}
