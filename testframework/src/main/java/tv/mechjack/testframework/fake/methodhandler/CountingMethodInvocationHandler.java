package tv.mechjack.testframework.fake.methodhandler;

import tv.mechjack.testframework.fake.Invocation;
import tv.mechjack.testframework.fake.MethodInvocationHandler;

public class CountingMethodInvocationHandler extends MethodInvocationHandlerDecorator {

  private int callCount;

  public CountingMethodInvocationHandler() {
    this(null);
  }

  public CountingMethodInvocationHandler(final MethodInvocationHandler invocationHandler) {
    super(invocationHandler);
    this.callCount = 0;
  }

  @Override
  protected boolean execute(final Invocation invocation) {
    this.callCount++;
    return true;
  }

  public final int getCallCount() {
    return this.callCount;
  }

}
