package tv.mechjack.platform.protobuf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.testframework.ArbitraryData.ARBITRARY_COLLECTION_SIZE;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.Message;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;

public abstract class BaseMessageStoreContractTests<K extends Message, V extends Message> {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestProtobufModule());
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

  protected abstract BaseMessageStore<K, V> givenASubjectToTest();

  protected abstract BaseMessageStore<K, V> givenASubjectToTest(Map<K, V> data);

  protected abstract K givenAKey();

  protected abstract V givenAValue();

  private Set<K> givenAKeySet() {
    final Set<K> keys = Sets.newHashSet();

    for (int i = 0; i < ARBITRARY_COLLECTION_SIZE; i++) {
      keys.add(this.givenAKey());
    }
    return keys;
  }

  @Test
  public final void containsKey_nullKey_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.containsKey(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("key"));
  }

  @Test
  public final void containsKey_noData_returnsFalse() {
    this.installModules();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.containsKey(this.givenAKey());

    assertThat(result).isFalse();
  }

  @Test
  public final void containsKey_withDataForKey_returnsTrue() {
    this.installModules();
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    data.put(key, this.givenAValue());
    final BaseMessageStore<K, V> subjectUnderTest = this
        .givenASubjectToTest(data);

    final boolean result = subjectUnderTest.containsKey(key);

    assertThat(result).isTrue();
  }

  @Test
  public final void containsKey_withDataButNotForKey_returnsFalse() {
    this.installModules();
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    data.put(key, this.givenAValue());
    final BaseMessageStore<K, V> subjectUnderTest = this
        .givenASubjectToTest(data);

    final boolean result = subjectUnderTest.containsKey(this.givenAKey());

    assertThat(result).isFalse();
  }

  @Test
  public final void getKeys_noData_returnsEmptyCollection() {
    this.installModules();
    final MessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Collection<K> result = subjectUnderTest.getKeys();

    assertThat(result).isEmpty();
  }

  @Test
  public final void getKeys_withData_returnsCollectionOfKeys() {
    this.installModules();
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
    this.installModules();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.get(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("key"));
  }

  @Test
  public final void get_noData_returnsEmptyOptional() {
    this.installModules();
    final K key = this.givenAKey();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).isEmpty();
  }

  @Test
  public final void get_withDataForKey_returnsValueForKey() {
    this.installModules();
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, value);
    final BaseMessageStore<K, V> subjectUnderTest = this
        .givenASubjectToTest(data);

    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).hasValue(value);
  }

  @Test
  public final void get_withDataButNotForKey_returnsEmptyOptional() {
    this.installModules();
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, value);
    final BaseMessageStore<K, V> subjectUnderTest = this
        .givenASubjectToTest(data);

    final Optional<V> result = subjectUnderTest.get(this.givenAKey());

    assertThat(result).isEmpty();
  }

  @Test
  public final void put_withNullKey_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.put(null, this.givenAValue()));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("key"));
  }

  @Test
  public final void put_withNullValue_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.put(this.givenAKey(), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("value"));
  }

  @Test
  public final void put_noData_putsValueForKey() {
    this.installModules();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.put(key, value);
    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).hasValue(value);
  }

  @Test
  public final void put_withDataForKey_replacesExistingValue() {
    this.installModules();
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, this.givenAValue());
    final BaseMessageStore<K, V> subjectUnderTest = this
        .givenASubjectToTest(data);

    subjectUnderTest.put(key, value);
    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).hasValue(value);
  }

  @Test
  public final void remove_withNullKey_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.remove(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("key"));
  }

  @Test
  public final void remove_noData_returnsSilently() {
    this.installModules();
    final BaseMessageStore<K, V> subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.remove(this.givenAKey()));

    assertThat(thrown).isNull();
  }

  @Test
  public final void remove_withDataForKey_removesValue() {
    this.installModules();
    final Map<K, V> data = Maps.newHashMap();
    final K key = this.givenAKey();
    final V value = this.givenAValue();
    data.put(key, value);
    final BaseMessageStore<K, V> subjectUnderTest = this
        .givenASubjectToTest(data);

    subjectUnderTest.remove(key);
    final Optional<V> result = subjectUnderTest.get(key);

    assertThat(result).isEmpty();
  }

  @Test
  public final void remove_withDataButNotForKey_makesNoChanges() {
    this.installModules();
    final Map<K, V> data = Maps.newHashMap();
    final Set<K> keys = this.givenAKeySet();
    for (final K key : keys) {
      data.put(key, this.givenAValue());
    }
    final BaseMessageStore<K, V> subjectUnderTest = this
        .givenASubjectToTest(data);

    subjectUnderTest.remove(this.givenAKey());
    final Collection<K> result = subjectUnderTest.getKeys();

    assertThat(result).containsExactlyInAnyOrderElementsOf(keys);
  }

}
