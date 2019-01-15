package tv.mechjack.platform.keyvaluestore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.util.OptionalByteArrayEqualsExpected;
import tv.mechjack.platform.util.TestUtilModule;
import tv.mechjack.testframework.TestFrameworkRule;

public abstract class KeyValueStoreContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

  private KeyValueStore givenASubjectToTest() {
    return this.givenASubjectToTest(this.givenData());
  }

  abstract KeyValueStore givenASubjectToTest(Map<byte[], byte[]> data);

  private Map<byte[], byte[]> givenData() {
    final Map<byte[], byte[]> data = new HashMap<>();

    for (int i = 0; i < 10; i++) {
      data.put(this.givenAByteArray(), this.givenAByteArray());
    }
    return data;
  }

  private byte[] givenAByteArray() {
    return this.testFrameworkRule.getArbitraryByteArray();
  }

  @Test
  public final void containsKey_nullKey_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.containsKey(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "key");
  }

  @Test
  public final void containsKey_keyPresent_returnsTrue() {
    this.installModules();
    final Map<byte[], byte[]> data = this.givenData();
    final byte[] key = this.givenAByteArray();
    data.put(key, this.givenAByteArray());
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    final boolean result = subjectUnderTest.containsKey(key);

    assertThat(result).isTrue();
  }

  @Test
  public final void containsKey_keyNotPresent_returnsTrue() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.containsKey(this.givenAByteArray());

    assertThat(result).isFalse();
  }

  @Test
  public final void getKeys_forSuppliedData_returnCollectionWithAllKeys() {
    this.installModules();
    final Map<byte[], byte[]> data = this.givenData();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    final Collection<byte[]> result = subjectUnderTest.getKeys();

    assertThat(result).containsExactlyInAnyOrderElementsOf(data.keySet());
  }

  @Test
  public final void getKeys_noData_returnsEmptySet() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(new HashMap<>());

    final Collection<byte[]> result = subjectUnderTest.getKeys();

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_nullKey_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "key");
  }

  @Test
  public final void get_noData_returnsEmptyOptional() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Optional<byte[]> result = subjectUnderTest.get(this.givenAByteArray());

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_withData_returnsOptionalWithValue() {
    this.installModules();
    final Map<byte[], byte[]> data = this.givenData();
    final byte[] key = this.givenAByteArray();
    final byte[] value = this.givenAByteArray();
    data.put(key, value);
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    final Optional<byte[]> result = subjectUnderTest.get(key);

    assertThat(result).is(new OptionalByteArrayEqualsExpected(value));
  }

  @Test
  public final void put_nullKey_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.put(null, this.givenAByteArray()));

    this.testFrameworkRule.assertNullPointerException(thrown, "key");
  }

  @Test
  public final void put_nullValue_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.put(this.givenAByteArray(), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "value");
  }

  @Test
  public final void put_noDataForKey_valueIsPut() {
    this.installModules();
    final byte[] key = this.givenAByteArray();
    final byte[] value = this.givenAByteArray();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.put(key, value);
    final Optional<byte[]> result = subjectUnderTest.get(key);

    assertThat(result).is(new OptionalByteArrayEqualsExpected(value));
  }

  @Test
  public final void put_withExistingValueForKey_newValueIsPut() {
    this.installModules();
    final Map<byte[], byte[]> data = this.givenData();
    final byte[] key = this.givenAByteArray();
    final byte[] value = this.givenAByteArray();
    data.put(key, this.givenAByteArray());
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    subjectUnderTest.put(key, value);
    final Optional<byte[]> result = subjectUnderTest.get(key);

    assertThat(result).is(new OptionalByteArrayEqualsExpected(value));
  }

  @Test
  public final void remove_nullKey_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.remove(null));

    this.testFrameworkRule.assertNullPointerException(thrown, "key");
  }

  @Test
  public final void remove_noDataForKey_returnsSilently() {
    this.installModules();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.remove(this.givenAByteArray()));

    assertThat(thrown).isNull();
  }

  @Test
  public final void remove_withExistingValueForKey_removesValue() {
    this.installModules();
    final Map<byte[], byte[]> data = this.givenData();
    final byte[] key = this.givenAByteArray();
    data.put(key, this.givenAByteArray());
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    subjectUnderTest.remove(key);

    assertThat(subjectUnderTest.get(key)).isEmpty();
  }

}
