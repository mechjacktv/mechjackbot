package com.mechjacktv.mechjackbot.configuration;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.HotUpdatePropertiesWrapperContractTests;
import com.mechjacktv.util.scheduleservice.ScheduleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

public class PropertiesAppConfigurationUnitTests extends HotUpdatePropertiesWrapperContractTests {

  private static final ExecutionUtils EXECUTION_UTILS = new DefaultExecutionUtils();

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private PropertiesAppConfiguration givenASubjectToTest() {
    return this.givenASubjectToTest(this::givenAPropertiesInputStream);
  }

  private PropertiesAppConfiguration givenASubjectToTest(final Supplier<InputStream> propertiesSupplier) {
    return this.givenASubjectToTest(propertiesSupplier, mock(ScheduleService.class));
  }

  @Override
  protected PropertiesAppConfiguration givenASubjectToTest(final Supplier<InputStream> propertiesSupplier,
      final ScheduleService scheduleService) {
    return new PropertiesAppConfiguration(propertiesSupplier, new DefaultExecutionUtils(), scheduleService);
  }

  @Test
  public final void get_nullKey_throwsNullPointerExceptionWithMessage() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void get_noValueForKey_returnsEmptyOptional() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Optional<String> result = subjectUnderTest.get(this.arbitraryDataGenerator.getString());

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_valueForKey_returnsOptionalWithValue() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Optional<String> result = subjectUnderTest.get(KEY_1);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> resultValue.equals(VALUE_1));
    softly.assertAll();
  }

  @Test
  public final void get_nullKeyWithDefaultValue_throwsNullPointerExceptionWithMessage() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null, this.arbitraryDataGenerator.getString()));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void get_forKeyWithNoValueAndDefaultValue_returnsDefaultValue() {
    final String defaultValue = this.arbitraryDataGenerator.getString();
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.arbitraryDataGenerator.getString(), defaultValue);

    assertThat(result).isEqualTo(defaultValue);
  }

  @Test
  public final void get_forKeyWithNoValueAndNullDefaultValue_returnsNull() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.arbitraryDataGenerator.getString(), null);

    assertThat(result).isNull();
  }

  @Test
  public final void get_forKeyWithValueAndDefaultValue_returnsKeyValue() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(KEY_1, this.arbitraryDataGenerator.getString());

    assertThat(result).isEqualTo(VALUE_1);
  }

  @Test
  public final void get_forKeyWithValueAndNullDefaultValue_returnsKeyValue() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(KEY_1, null);

    assertThat(result).isEqualTo(VALUE_1);
  }

  @Test
  public final void getKeys_whenDataPresent_returnsCollectionOfKeys() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Collection<String> result = subjectUnderTest.getKeys();

    assertThat(result).contains(KEY_1, KEY_2);
  }

  @Test
  public final void getKeys_noData_returnsEmptyCollection() {
    final PropertiesAppConfiguration subjectUnderTest = this.givenASubjectToTest(() -> mock(InputStream.class));

    final Collection<String> result = subjectUnderTest.getKeys();

    assertThat(result).isEmpty();
  }

}
