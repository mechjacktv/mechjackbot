package tv.mechjack.util.typedobject;

public class StronglyTypedInstantiationException extends RuntimeException {

  public StronglyTypedInstantiationException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

}
