package com.mechjacktv.mechjackbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public abstract class AppConfigurationContractTests {

  private static final Integer NUMBER_OF_PROPERTIES = 3;
  private static final ExecutionUtils EXECUTION_UTILS = new DefaultExecutionUtils();

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private AppConfiguration givenASubjectToTest() {
    return this.givenASubjectToTest(this.givenAPropertiesMap());
  }

  protected abstract AppConfiguration givenASubjectToTest(final Map<String, String> properties);

  private Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    for (int i = 0; i < NUMBER_OF_PROPERTIES; i++) {
      properties.put(this.arbitraryDataGenerator.getString(), this.arbitraryDataGenerator.getString());
    }
    return properties;
  }

  @Test
  public final void get_nullKey_throwsNullPointerExceptionWithMessage() {
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void get_noValueForKey_returnsEmptyOptional() {
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Optional<String> result = subjectUnderTest.get(this.arbitraryDataGenerator.getString());

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_withData_returnsOptionalWithValue() {
    final String key = this.arbitraryDataGenerator.getString();
    final String value = this.arbitraryDataGenerator.getString();
    final Map<String, String> properties = new HashMap<>();
    properties.put(key, value);
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final Optional<String> result = subjectUnderTest.get(key);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> resultValue.equals(value));
    softly.assertAll();
  }

  @Test
  public final void get_nullKeyWithDefaultValue_throwsNullPointerExceptionWithMessage() {
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null, this.arbitraryDataGenerator.getString()));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void get_forKeyWithNoValueAndDefaultValue_returnsDefaultValue() {
    final String defaultValue = this.arbitraryDataGenerator.getString();
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.arbitraryDataGenerator.getString(), defaultValue);

    assertThat(result).isEqualTo(defaultValue);
  }

  @Test
  public final void get_forKeyWithNoValueAndNullDefaultValue_returnsNull() {
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.arbitraryDataGenerator.getString(), null);

    assertThat(result).isNull();
  }

  @Test
  public final void get_forKeyWithValueAndDefaultValue_returnsKeyValue() {
    final String key = this.arbitraryDataGenerator.getString();
    final String value = this.arbitraryDataGenerator.getString();
    final Map<String, String> properties = new HashMap<>();
    properties.put(key, value);
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final String result = subjectUnderTest.get(key, this.arbitraryDataGenerator.getString());

    assertThat(result).isEqualTo(value);
  }

  @Test
  public final void get_forKeyWithValueAndNullDefaultValue_returnsKeyValue() {
    final String key = this.arbitraryDataGenerator.getString();
    final String value = this.arbitraryDataGenerator.getString();
    final Map<String, String> properties = new HashMap<>();
    properties.put(key, value);
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final String result = subjectUnderTest.get(key, null);

    assertThat(result).isEqualTo(value);
  }

  @Test
  public final void getKeys_whenDataPresent_returnsCollectionOfKeys() {
    final Map<String, String> properties = this.givenAPropertiesMap();
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest(properties);

    final Collection<String> result = subjectUnderTest.getKeys();

    assertThat(result).containsOnlyElementsOf(properties.keySet());
  }

  @Test
  public final void getKeys_noData_returnsEmptyCollection() {
    final AppConfiguration subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Collection<String> result = subjectUnderTest.getKeys();

    assertThat(result).isEmpty();
  }

}
