package tv.mechjack.testframework.fake;

import java.lang.reflect.Method;

public class DefaultInvocation implements Invocation {

  private final Object fake;
  private final Method method;
  private final Object[] arguments;

  public DefaultInvocation(final Object fake, final Method method, final Object[] arguments) {
    this.fake = fake;
    this.method = method;
    this.arguments = arguments;
  }

  @Override
  public Object[] getArguments() {
    return this.arguments;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getArgument(final int index) {
    return (T) this.arguments[index];
  }

  @Override
  public Method getMethod() {
    return this.method;
  }

  @Override
  public Object getFake() {
    return this.fake;
  }

}
