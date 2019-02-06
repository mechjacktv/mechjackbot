package tv.mechjack.testframework.fake;

public class FakeInstantiationException extends RuntimeException {

  public FakeInstantiationException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

}
