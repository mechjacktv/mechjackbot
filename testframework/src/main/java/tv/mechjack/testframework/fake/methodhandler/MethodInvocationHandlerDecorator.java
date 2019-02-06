package tv.mechjack.testframework.fake.methodhandler;

import java.util.Objects;

import tv.mechjack.testframework.fake.Invocation;
import tv.mechjack.testframework.fake.MethodInvocationHandler;

public abstract class MethodInvocationHandlerDecorator implements MethodInvocationHandler {

  private final MethodInvocationHandler nextHandler;

  public MethodInvocationHandlerDecorator() {
    this(null);
  }

  public MethodInvocationHandlerDecorator(final MethodInvocationHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public final Object apply(final Invocation invocation) throws Throwable {
    final boolean callNext = execute(invocation);

    if (callNext && Objects.nonNull(this.nextHandler)) {
      return this.nextHandler.apply(invocation);
    }
    return null;
  }

  protected abstract boolean execute(final Invocation invocation) throws Throwable;

}
