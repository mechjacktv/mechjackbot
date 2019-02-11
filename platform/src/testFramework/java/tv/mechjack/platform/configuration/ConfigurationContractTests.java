package tv.mechjack.platform.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.testframework.ArbitraryData.ARBITRARY_COLLECTION_SIZE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;

public abstract class ConfigurationContractTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

  private Configuration givenASubjectToTest() throws Exception {
    return this.givenASubjectToTest(this.givenAPropertiesMap());
  }

  protected abstract Configuration givenASubjectToTest(final Map<String, String> properties) throws Exception;

  private Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      properties.put(this.testFrameworkRule.arbitraryData().getString(),
          this.testFrameworkRule.arbitraryData().getString());
    }
    return properties;
  }

  @Test
  public final void get_nullKey_throwsNullPointerExceptionWithMessage() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get((String) null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("key"));
  }

  @Test
  public final void get_noValueForKey_returnsEmptyOptional() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final Optional<String> result = subjectUnderTest.get(this.testFrameworkRule.arbitraryData().getString());

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_withData_returnsOptionalWithValue() throws Exception {
    this.installModules();
    final String key = this.testFrameworkRule.arbitraryData().getString();
    final String value = this.testFrameworkRule.arbitraryData().getString();
    final Map<String, String> properties = new HashMap<>();
    properties.put(key, value);
    final Configuration subjectUnderTest = this.givenASubjectToTest(properties);

    final Optional<String> result = subjectUnderTest.get(key);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> resultValue.equals(value));
    softly.assertAll();
  }

  @Test
  public final void get_nullKeyWithDefaultValue_throwsNullPointerExceptionWithMessage() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get((String) null,
        this.testFrameworkRule.arbitraryData().getString()));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("key"));
  }

  @Test
  public final void get_forKeyWithNoValueAndDefaultValue_returnsDefaultValue() throws Exception {
    this.installModules();
    final String defaultValue = this.testFrameworkRule.arbitraryData().getString();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.testFrameworkRule.arbitraryData().getString(), defaultValue);

    assertThat(result).isEqualTo(defaultValue);
  }

  @Test
  public final void get_forKeyWithNoValueAndNullDefaultValue_returnsNull() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.testFrameworkRule.arbitraryData().getString(), null);

    assertThat(result).isNull();
  }

  @Test
  public final void get_forKeyWithValueAndDefaultValue_returnsKeyValue() throws Exception {
    this.installModules();
    final String key = this.testFrameworkRule.arbitraryData().getString();
    final String value = this.testFrameworkRule.arbitraryData().getString();
    final Map<String, String> properties = new HashMap<>();
    properties.put(key, value);
    final Configuration subjectUnderTest = this.givenASubjectToTest(properties);

    final String result = subjectUnderTest.get(key, this.testFrameworkRule.arbitraryData().getString());

    assertThat(result).isEqualTo(value);
  }

  @Test
  public final void get_forKeyWithValueAndNullDefaultValue_returnsKeyValue() throws Exception {
    this.installModules();
    final String key = this.testFrameworkRule.arbitraryData().getString();
    final String value = this.testFrameworkRule.arbitraryData().getString();
    final Map<String, String> properties = new HashMap<>();
    properties.put(key, value);
    final Configuration subjectUnderTest = this.givenASubjectToTest(properties);

    final String result = subjectUnderTest.get(key, null);

    assertThat(result).isEqualTo(value);
  }

  @Test
  public final void getKeys_whenDataPresent_returnsCollectionOfKeys() throws Exception {
    this.installModules();
    final Map<String, String> properties = this.givenAPropertiesMap();
    final Configuration subjectUnderTest = this.givenASubjectToTest(properties);

    final Collection<String> result = subjectUnderTest.getKeys();

    assertThat(result).containsOnlyElementsOf(properties.keySet());
  }

  @Test
  public final void getKeys_noData_returnsEmptyCollection() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Collection<String> result = subjectUnderTest.getKeys();

    assertThat(result).isEmpty();
  }

}
