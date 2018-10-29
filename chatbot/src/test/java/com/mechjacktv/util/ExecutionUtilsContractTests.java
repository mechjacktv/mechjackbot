package com.mechjacktv.util;

import com.mechjacktv.util.function.SupplierWithException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public abstract class ExecutionUtilsContractTests {

  abstract ExecutionUtils givenASubjectToTest();

  @Test
  public final void softenException_withRunnableNoException_completesNormally() {
    final ExecutionUtils subjectUnderTest = givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(() -> {
      // do nothing
    }, RuntimeException.class));

    assertThat(thrown).isNull();
  }

  @Test
  public final void softenException_withRunnableThrowsException_throwsRuntimeException() {
    final ExecutionUtils subjectUnderTest = givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(() -> {
      throw new Exception("test exception");
    }, RuntimeException.class));

    assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessageContaining("test exception");
  }

  @Test
  public final void softenException_withSupplierNoException_returnsSuppliedObject() {
    final Object suppliedObject = new Object();
    final ExecutionUtils subjectUnderTest = givenASubjectToTest();

    final Object result = subjectUnderTest.softenException(() -> {
      return suppliedObject;
    }, RuntimeException.class);

    assertThat(result).isEqualTo(suppliedObject);
  }

  @Test
  public final void softenException_withSupplierThrowsException_throwsRuntimeException() {
    final ExecutionUtils subjectUnderTest = givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(this::throwsException,
        RuntimeException.class));

    assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessageContaining("test exception");
  }

  private SupplierWithException<Object> throwsException() throws Exception {
    throw new Exception("test exception");
  }

}
