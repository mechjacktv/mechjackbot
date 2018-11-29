package com.mechjacktv.util.typedobject;

public class NoValidStronglyTypedConstructorException extends RuntimeException {

  private static final long serialVersionUID = 4289853677116020894L;

  public NoValidStronglyTypedConstructorException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

}
