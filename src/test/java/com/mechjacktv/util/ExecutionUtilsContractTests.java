package com.mechjacktv.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import com.mechjacktv.util.function.SupplierWithException;

public abstract class ExecutionUtilsContractTests {

  private static final class TestableRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -2149332864017817028L;

    public TestableRuntimeException(Throwable cause) {
      this(cause.getMessage(), cause);
    }

    public TestableRuntimeException(String message, Throwable cause) {
      super(message, cause);
    }

  }

  @Test
  public final void softenException_withRunnableNoException_completesNormally() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(() -> {
      // do nothing
    }, TestableRuntimeException.class));

    assertThat(thrown).isNull();
  }

  abstract ExecutionUtils givenASubjectToTest();

  @Test
  public final void softenException_withRunnableThrowsException_throwsRuntimeException() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(() -> {
      throw new Exception("test exception");
    }, TestableRuntimeException.class));

    assertThat(thrown).isInstanceOf(TestableRuntimeException.class).hasMessageContaining("test exception");
  }

  @Test
  public final void softenException_withSupplierNoException_returnsSuppliedObject() {
    final Object suppliedObject = new Object();
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Object result = subjectUnderTest.softenException(() -> suppliedObject, TestableRuntimeException.class);

    assertThat(result).isEqualTo(suppliedObject);
  }

  @Test
  public final void softenException_withSupplierThrowsException_throwsRuntimeException() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(this::throwsException,
        TestableRuntimeException.class));

    assertThat(thrown).isInstanceOf(TestableRuntimeException.class).hasMessageContaining("test exception");
  }

  private SupplierWithException<Object> throwsException() throws Exception {
    throw new Exception("test exception");
  }

}
