package tv.mechjack.platform.configuration;

import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import tv.mechjack.platform.utils.ExecutionUtils;

public class MapConfigurationUnitTests extends ConfigurationContractTests {

  private MapConfiguration givenASubjectToTest() {
    return this.givenASubjectToTest(new HashMap<>());
  }

  @Override
  protected MapConfiguration givenASubjectToTest(Map<String, String> properties) {
    final MapConfiguration appConfiguration = new MapConfiguration(
        this.testFrameworkRule.getInstance(ExecutionUtils.class));

    for (final String key : properties.keySet()) {
      appConfiguration.set(key, properties.get(key));
    }
    return appConfiguration;
  }

  @Test
  public final void set_nullKey_thrownNullPointerException() {
    this.installModules();
    final MapConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.set((String) null,
        this.testFrameworkRule.getArbitraryString()));

    this.testFrameworkRule.assertNullPointerException(thrown, "key");
  }

  @Test
  public final void set_nullValue_thrownNullPointerException() {
    this.installModules();
    final MapConfiguration subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.set(this.testFrameworkRule.getArbitraryString(), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "value");
  }

  @Test
  public final void set_withKeyValuePair_storesValue() {
    this.installModules();
    final String key = this.testFrameworkRule.getArbitraryString();
    final String value = this.testFrameworkRule.getArbitraryString();
    final MapConfiguration subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.set(key, value);
    final Optional<String> result = subjectUnderTest.get(key);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> softly.assertThat(resultValue).isEqualTo(value));
    softly.assertAll();
  }

}
