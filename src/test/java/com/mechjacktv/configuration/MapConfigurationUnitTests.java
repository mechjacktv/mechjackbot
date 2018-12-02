package com.mechjacktv.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public class MapConfigurationUnitTests extends ConfigurationContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private MapConfiguration givenASubjectToTest() {
    return this.givenASubjectToTest(new HashMap<>());
  }

  @Override
  protected MapConfiguration givenASubjectToTest(Map<String, String> properties) {
    final MapConfiguration appConfiguration = new MapConfiguration(this.executionUtils);

    for (final String key : properties.keySet()) {
      appConfiguration.set(key, properties.get(key));
    }
    return appConfiguration;
  }

  @Test
  public final void set_nullKey_thrownNullPointerException() {
    final MapConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.set((String) null,
        this.arbitraryDataGenerator.getString()));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("key"));
  }

  @Test
  public final void set_nullValue_thrownNullPointerException() {
    final MapConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.set(this.arbitraryDataGenerator.getString(), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("value"));
  }

  @Test
  public final void set_withKeyValuePair_storesValue() {
    final String key = this.arbitraryDataGenerator.getString();
    final String value = this.arbitraryDataGenerator.getString();
    final MapConfiguration subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.set(key, value);
    final Optional<String> result = subjectUnderTest.get(key);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> softly.assertThat(resultValue).isEqualTo(value));
    softly.assertAll();
  }

}
