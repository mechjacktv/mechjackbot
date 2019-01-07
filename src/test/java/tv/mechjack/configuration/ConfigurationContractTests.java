package tv.mechjack.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.util.TestUtilModule;

public abstract class ConfigurationContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

  private Configuration givenASubjectToTest() throws Exception {
    return this.givenASubjectToTest(this.givenAPropertiesMap());
  }

  protected abstract Configuration givenASubjectToTest(final Map<String, String> properties) throws Exception;

  private Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    for (int i = 0; i < TestFrameworkRule.ARBITRARY_COLLECTION_SIZE; i++) {
      properties.put(this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString());
    }
    return properties;
  }

  @Test
  public final void get_nullKey_throwsNullPointerExceptionWithMessage() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get((String) null));

    this.testFrameworkRule.assertNullPointerException(thrown, "key");
  }

  @Test
  public final void get_noValueForKey_returnsEmptyOptional() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final Optional<String> result = subjectUnderTest.get(this.testFrameworkRule.getArbitraryString());

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_withData_returnsOptionalWithValue() throws Exception {
    this.installModules();
    final String key = this.testFrameworkRule.getArbitraryString();
    final String value = this.testFrameworkRule.getArbitraryString();
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
        this.testFrameworkRule.getArbitraryString()));

    this.testFrameworkRule.assertNullPointerException(thrown, "key");
  }

  @Test
  public final void get_forKeyWithNoValueAndDefaultValue_returnsDefaultValue() throws Exception {
    this.installModules();
    final String defaultValue = this.testFrameworkRule.getArbitraryString();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.testFrameworkRule.getArbitraryString(), defaultValue);

    assertThat(result).isEqualTo(defaultValue);
  }

  @Test
  public final void get_forKeyWithNoValueAndNullDefaultValue_returnsNull() throws Exception {
    this.installModules();
    final Configuration subjectUnderTest = this.givenASubjectToTest();

    final String result = subjectUnderTest.get(this.testFrameworkRule.getArbitraryString(), null);

    assertThat(result).isNull();
  }

  @Test
  public final void get_forKeyWithValueAndDefaultValue_returnsKeyValue() throws Exception {
    this.installModules();
    final String key = this.testFrameworkRule.getArbitraryString();
    final String value = this.testFrameworkRule.getArbitraryString();
    final Map<String, String> properties = new HashMap<>();
    properties.put(key, value);
    final Configuration subjectUnderTest = this.givenASubjectToTest(properties);

    final String result = subjectUnderTest.get(key, this.testFrameworkRule.getArbitraryString());

    assertThat(result).isEqualTo(value);
  }

  @Test
  public final void get_forKeyWithValueAndNullDefaultValue_returnsKeyValue() throws Exception {
    this.installModules();
    final String key = this.testFrameworkRule.getArbitraryString();
    final String value = this.testFrameworkRule.getArbitraryString();
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
