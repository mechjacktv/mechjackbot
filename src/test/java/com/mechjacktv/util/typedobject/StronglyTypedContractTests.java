package com.mechjacktv.util.typedobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

public abstract class StronglyTypedContractTests<T> {

  @Test
  public final void new_notNullValue_wrapsValue() {
    final T value = this.givenIHaveAValue();

    final StronglyTyped<?> result = this.givenIHaveAStronglyTypedValue(value);

    assertThat(result.value).isEqualTo(value);
  }

  protected abstract T givenIHaveAValue();

  protected abstract T givenIHaveADifferentValue();

  private StronglyTyped<?> givenIHaveAStronglyTypedValue() {
    return this.givenIHaveAStronglyTypedValue(this.givenIHaveAValue());
  }

  protected abstract StronglyTyped<?> givenIHaveAStronglyTypedValue(final T value);

  @Test
  public final void new_nullValue_throwsNullPointerException() {
    final Throwable result = catchThrowable(() -> this.givenIHaveAStronglyTypedValue(null));

    assertThat(result).isInstanceOf(NullPointerException.class);
  }

  @Test
  public final void equals_nullValue_returnsFalse() {
    final StronglyTyped<?> subjectUnderTest = this.givenIHaveAStronglyTypedValue();

    final boolean result = subjectUnderTest.equals(null);

    assertThat(result).isFalse();
  }

  @Test
  public final void equals_objectNotOfSameType_returnsFalse() {
    final StronglyTyped<?> subjectUnderTest = this.givenIHaveAStronglyTypedValue();

    final boolean result = subjectUnderTest.equals(new Object());

    assertThat(result).isFalse();
  }

  @Test
  public final void equals_objectWithDifferentValue_returnsFalse() {
    final StronglyTyped<?> objectToCompare = this.givenIHaveAStronglyTypedValue(this.givenIHaveADifferentValue());
    final StronglyTyped<?> subjectUnderTest = this.givenIHaveAStronglyTypedValue();

    final boolean result = subjectUnderTest.equals(objectToCompare);

    assertThat(result).isFalse();
  }

  @Test
  public final void equals_objectWithSameValue_returnsTrue() {
    final T value = this.givenIHaveAValue();
    final StronglyTyped<?> objectToCompare = this.givenIHaveAStronglyTypedValue(value);
    final StronglyTyped<?> subjectUnderTest = this.givenIHaveAStronglyTypedValue(value);

    final boolean result = subjectUnderTest.equals(objectToCompare);

    assertThat(result).isTrue();
  }

}
