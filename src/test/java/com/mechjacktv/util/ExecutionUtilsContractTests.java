package com.mechjacktv.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import com.mechjacktv.util.function.SupplierWithException;

public abstract class ExecutionUtilsContractTests {

  private static final String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";

  @Test
  public final void softenException_withRunnableNoException_completesNormally() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(() -> {
      // do nothing
    }, TestableException.class));

    assertThat(thrown).isNull();
  }

  abstract ExecutionUtils givenASubjectToTest();

  @Test
  public final void softenException_withRunnableThrowsException_throwsTestableException() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(
        () -> {
          throw new Exception(EXCEPTION_MESSAGE);
        }, TestableException.class));

    assertThat(thrown).isInstanceOf(TestableException.class).hasMessage(EXCEPTION_MESSAGE);
  }

  @Test
  public final void softenException_withSupplierNoException_returnsSuppliedObject() {
    final Object suppliedObject = new Object();
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Object result = subjectUnderTest.softenException(() -> suppliedObject, TestableException.class);

    assertThat(result).isEqualTo(suppliedObject);
  }

  @Test
  public final void softenException_withSupplierThrowsException_throwsTestableException() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(this::supplierThrowsException,
        TestableException.class));

    assertThat(thrown).isInstanceOf(TestableException.class).hasMessage(EXCEPTION_MESSAGE);
  }

  private SupplierWithException<Object> supplierThrowsException() throws Exception {
    throw new Exception(EXCEPTION_MESSAGE);
  }

  private static final class TestableException extends RuntimeException {

    private static final long serialVersionUID = -2149332864017817028L;

    public TestableException(String message, Throwable cause) {
      super(message, cause);
    }

  }

}
