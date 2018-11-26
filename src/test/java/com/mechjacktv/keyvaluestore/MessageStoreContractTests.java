package com.mechjacktv.keyvaluestore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.Message;

import org.junit.Test;

import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public abstract class MessageStoreContractTests<K extends Message, V extends Message> {

  public final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  protected abstract K givenAKey();

  protected abstract V givenAValue();

  private Set<K> givenAKeySet() {
    final Set<K> keys = Sets.newHashSet();

    for (int i = 0; i < 10; i++) {
      keys.add(this.givenAKey());
    }
    return keys;
  }

  private AbstractMessageStore<K, V> givenASubjectToTest() {
    return this.givenASubjectToTest(Maps.newHashMap());
  }

  protected abstract AbstractMessageStore<K, V> givenASubjectToTest(Map<K, V> data);

  @Test
  public final void containsKey_nullKey_throwsNullPointerExceptionWithMessage() {
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.containsKey(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("key"));
  }

  @Test
  public final void containsKey_noData_returnsFalse() {
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.containsKey(this.givenAKey());

    assertThat(result).isFalse();
  }

  @Test
  public final void containsKey_withDataForKey_returnsTrue() {
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    data.put(key, this.givenAValue());
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    final boolean result = subjectUnderTest.containsKey(key);

    assertThat(result).isTrue();
  }

  @Test
  public final void containsKey_withDataButNotForKey_returnsFalse() {
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    data.put(key, this.givenAValue());
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    final boolean result = subjectUnderTest.containsKey(this.givenAKey());

    assertThat(result).isFalse();
  }

  @Test
  public final void getKeys_noData_returnsEmptyCollection() {
    final MessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Collection<K> result = subjectUnderTest.getKeys();

    assertThat(result).isEmpty();
  }

  @Test
  public final void getKeys_withData_returnsCollectionOfKeys() {
    final Map<K, V> data = Maps.newHashMap();
    final Set<K> keys = this.givenAKeySet();
    for (final K key : keys) {
      data.put(key, this.givenAValue());
    }
    final MessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    final Collection<K> result = subjectUnderTest.getKeys();

    assertThat(result).containsExactlyInAnyOrderElementsOf(keys);
  }

  @Test
  public final void get_nullKey_throwsNullPointerExceptionWithMessage() {
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("key"));
  }

  @Test
  public final void get_noData_returnsEmptyOptional() {
    final K key = this.givenAKey();
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_withDataForKey_returnsValueForKey() {
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, value);
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).hasValue(value);
  }

  @Test
  public final void get_withDataButNotForKey_returnsEmptyOptional() {
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, value);
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    final Optional<V> result = subjectUnderTest.get(this.givenAKey());

    assertThat(result).isEmpty();
  }

  @Test
  public final void put_withNullKey_throwsNullPointerExceptionWithMessage() {
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.put(null, this.givenAValue()));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("key"));
  }

  @Test
  public final void put_withNullValue_throwsNullPointerExceptionWithMessage() {
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.put(this.givenAKey(), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("value"));
  }

  @Test
  public final void put_noData_putsValueForKey() {
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.put(key, value);
    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).hasValue(value);
  }

  @Test
  public final void put_withDataForKey_replacesExistingValue() {
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, this.givenAValue());
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    subjectUnderTest.put(key, value);
    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).hasValue(value);
  }

  @Test
  public final void remove_withNullKey_throwsNullPointerExceptionWithMessage() {
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.remove(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("key"));
  }

  @Test
  public final void remove_noData_returnsSilently() {
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.remove(this.givenAKey()));

    assertThat(thrown).isNull();
  }

  @Test
  public final void remove_withDataForKey_removesValue() {
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, value);
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    subjectUnderTest.remove(key);
    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).isEmpty();
  }

  @Test
  public final void remove_withDataButNotForKey_makesNoChanges() {
    final Map<K, V> data = Maps.newHashMap();
    final Set<K> keys = this.givenAKeySet();
    for (final K key : keys) {
      data.put(key, this.givenAValue());
    }
    final AbstractMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest(data);

    subjectUnderTest.remove(this.givenAKey());
    final Collection<K> result = subjectUnderTest.getKeys();

    assertThat(result).containsExactlyInAnyOrderElementsOf(keys);
  }

}
