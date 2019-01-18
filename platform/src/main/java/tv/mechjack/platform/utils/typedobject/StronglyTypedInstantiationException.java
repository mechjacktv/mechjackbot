package tv.mechjack.platform.utils.typedobject;

public class StronglyTypedInstantiationException extends RuntimeException {

  public StronglyTypedInstantiationException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

}
