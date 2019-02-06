package tv.mechjack.testframework.fake;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class InstanceInvocationHandler implements InvocationHandler {

  private final Map<Method, MethodInvocationHandler> handlers = new HashMap<>();

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    if (this.handlers.containsKey(method)) {
      return this.handlers.get(method).apply(new DefaultInvocation(proxy, method, args));
    }
    return null;
  }

  public void addHandler(final Method method, final MethodInvocationHandler handler) {
    this.handlers.put(method, handler);
  }

}
