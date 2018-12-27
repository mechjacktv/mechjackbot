package tv.mechjack.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.util.function.SupplierWithException;

public abstract class ExecutionUtilsContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  abstract ExecutionUtils givenASubjectToTest();

  @Test
  public final void softenException_withRunnableNoException_completesNormally() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(() -> {
      // do nothing
    }, WrappingException.class));

    assertThat(thrown).isNull();
  }

  @Test
  public final void softenException_withRunnableThrowsException_throwsWrappingException() {
    final String exceptionMessage = this.testFrameworkRule.getArbitraryString();
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(
        () -> {
          throw new Exception(exceptionMessage);
        }, WrappingException.class));

    assertThat(thrown).isInstanceOf(WrappingException.class).hasMessage(exceptionMessage);
  }

  @Test
  public final void softenException_withRunnableThrowsExceptionNonWrappingException_throwsSoftenedException() {
    final String exceptionMessage = this.testFrameworkRule.getArbitraryString();
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(
        () -> {
          throw new Exception(exceptionMessage);
        }, NonWrappingException.class));

    assertThat(thrown).isInstanceOf(SoftenedException.class).hasMessage(exceptionMessage);
  }

  @Test
  public final void softenException_withSupplierNoException_returnsSuppliedObject() {
    final Object suppliedObject = new Object();
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Object result = subjectUnderTest.softenException(() -> suppliedObject, WrappingException.class);

    assertThat(result).isEqualTo(suppliedObject);
  }

  @Test
  public final void softenException_withSupplierThrowsException_throwsWrappingException() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(this::supplierThrowsException,
        WrappingException.class));

    assertThat(thrown).isInstanceOf(WrappingException.class);
  }

  @Test
  public final void softenException_withSupplierThrowsExceptionNonWrappingException_throwsSoftenedException() {
    final ExecutionUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.softenException(this::supplierThrowsException,
        NonWrappingException.class));

    assertThat(thrown).isInstanceOf(SoftenedException.class);
  }

  private SupplierWithException<Object> supplierThrowsException() throws Exception {
    throw new Exception(this.testFrameworkRule.getArbitraryString());
  }

  private static final class WrappingException extends RuntimeException {

    private static final long serialVersionUID = -2149332864017817028L;

    public WrappingException(final String message, final Throwable cause) {
      super(message, cause);
    }

  }

  private static final class NonWrappingException extends RuntimeException {

    private static final long serialVersionUID = 5785191548998147302L;

    public NonWrappingException(final Throwable cause) {
      super(cause);
    }

  }

}
