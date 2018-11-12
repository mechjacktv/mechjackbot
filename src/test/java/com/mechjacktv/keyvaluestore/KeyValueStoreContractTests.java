package com.mechjacktv.keyvaluestore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.*;

import com.google.common.collect.Maps;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public abstract class KeyValueStoreContractTests {

  private static final ExecutionUtils EXECUTION_UTILS = new DefaultExecutionUtils();
  private static final Random RANDOM = new Random();

  private KeyValueStore givenASubjectToTest() {
    return this.givenASubjectToTest(Maps.newHashMap());
  }

  abstract KeyValueStore givenASubjectToTest(Map<byte[], byte[]> data);

  private byte[] givenAByteArray() {
    final byte[] byteArray = new byte[64];
    RANDOM.nextBytes(byteArray);

    return byteArray;
  }

  @Test
  public final void containsKey_nullKey_throwsNullPointerExceptionWithMessage() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.containsKey(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void containsKey_keyPresent_returnsTrue() {
    final Map<byte[], byte[]> data = Maps.newHashMap();
    final byte[] key = this.givenAByteArray();
    data.put(key, this.givenAByteArray());
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    final boolean result = subjectUnderTest.containsKey(key);

    assertThat(result).isTrue();
  }

  @Test
  public final void containsKey_keyNotPresent_returnsTrue() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.containsKey(this.givenAByteArray());

    assertThat(result).isFalse();
  }

  @Test
  public final void getKeys_forSuppliedData_returnCollectionWithAllKeys() {
    final Map<byte[], byte[]> data = Maps.newHashMap();
    final byte[] key1 = this.givenAByteArray();
    final byte[] key2 = this.givenAByteArray();
    final byte[] key3 = this.givenAByteArray();
    data.put(key1, this.givenAByteArray());
    data.put(key2, this.givenAByteArray());
    data.put(key3, this.givenAByteArray());
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    final Collection<byte[]> result = subjectUnderTest.getKeys();

    assertThat(result).contains(key1, key2, key3);
  }

  @Test
  public final void getKeys_noData_returnsEmptySet() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Collection<byte[]> result = subjectUnderTest.getKeys();

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_nullKey_throwsNullPointerExceptionWithMessage() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void get_noData_returnsEmptyOptional() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Optional<byte[]> result = subjectUnderTest.get(this.givenAByteArray());

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_withData_returnsOptionalWithValue() {
    final Map<byte[], byte[]> data = Maps.newHashMap();
    final byte[] key = this.givenAByteArray();
    final byte[] value = this.givenAByteArray();
    data.put(key, value);
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    final Optional<byte[]> result = subjectUnderTest.get(key);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> softly.assertThat(Arrays.equals(resultValue, value)));
    softly.assertAll();
  }

  @Test
  public final void put_nullKey_throwsNullPointerExceptionWithMessage() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.put(null, this.givenAByteArray()));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void put_nullValue_throwsNullPointerExceptionWithMessage() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.put(this.givenAByteArray(), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("value"));
  }

  @Test
  public final void put_noDataForKey_valueIsPut() {
    final byte[] key = this.givenAByteArray();
    final byte[] value = this.givenAByteArray();
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.put(key, value);
    final Optional<byte[]> result = subjectUnderTest.get(key);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> softly.assertThat(Arrays.equals(resultValue, value)));
    softly.assertAll();
  }

  @Test
  public final void put_withExistingValueForKey_newValueIsPut() {
    final Map<byte[], byte[]> data = Maps.newHashMap();
    final byte[] key = this.givenAByteArray();
    final byte[] value1 = this.givenAByteArray();
    final byte[] value2 = this.givenAByteArray();
    data.put(key, value1);
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    subjectUnderTest.put(key, value2);
    final Optional<byte[]> result = subjectUnderTest.get(key);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultValue) -> softly.assertThat(Arrays.equals(resultValue, value2)));
    softly.assertAll();
  }

  @Test
  public final void remove_nullKey_throwsNullPointerExceptionWithMessage() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.remove(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class).hasMessage(EXECUTION_UTILS.nullMessageForName("key"));
  }

  @Test
  public final void remove_noDataForKey_returnsSilently() {
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.remove(this.givenAByteArray()));

    assertThat(thrown).isNull();
  }

  @Test
  public final void remove_withExistingValueForKey_removesValue() {
    final Map<byte[], byte[]> data = Maps.newHashMap();
    final byte[] key = this.givenAByteArray();
    final byte[] value = this.givenAByteArray();
    data.put(key, value);
    final KeyValueStore subjectUnderTest = this.givenASubjectToTest(data);

    subjectUnderTest.remove(key);
    final Optional<byte[]> result = subjectUnderTest.get(key);

    assertThat(result).isEmpty();
  }

}
