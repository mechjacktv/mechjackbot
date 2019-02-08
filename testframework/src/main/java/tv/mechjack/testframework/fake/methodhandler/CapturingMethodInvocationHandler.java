package tv.mechjack.testframework.fake.methodhandler;

import tv.mechjack.testframework.fake.Invocation;
import tv.mechjack.testframework.fake.MethodInvocationHandler;

public class CapturingMethodInvocationHandler extends MethodInvocationHandlerDecorator {

  private final int index;
  private Object value;

  public CapturingMethodInvocationHandler(final int index) {
    this(index, null);
  }

  public CapturingMethodInvocationHandler(final int index, final MethodInvocationHandler nextHandler) {
    super(nextHandler);
    this.index = index;
    this.value = null;
  }

  @Override
  protected boolean execute(final Invocation invocation) throws Throwable {
    this.value = invocation.getArgument(this.index);
    return true;
  }

  @SuppressWarnings("unchecked")
  public final <T> T getValue() {
    return (T) this.value;
  }

}
