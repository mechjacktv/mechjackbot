package tv.mechjack.testframework.fake;

import java.lang.reflect.Method;

public interface FakeBuilder<T> {

  MethodHandlerBuilder<T> forMethod(String methodName, Class<?>... parameterTypes);

  MethodHandlerBuilder<T> forMethod(Method method);

  T build();

  interface MethodHandlerBuilder<T> {

    FakeBuilder<T> addHandler(MethodInvocationHandler handler);

  }

}
