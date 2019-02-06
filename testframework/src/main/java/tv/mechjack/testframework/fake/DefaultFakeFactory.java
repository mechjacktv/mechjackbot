package tv.mechjack.testframework.fake;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DefaultFakeFactory implements FakeFactory {

  @Override
  public <T> FakeBuilder<T> builder(final Class<T> type) {
    return new DefaultFakeBuilder<>(this, type);
  }

  public final <T> T fake(final Class<T> type) {
    return this.fake(type, new InstanceInvocationHandler());
  }

  public final <T> T fake(Class<T> type, final InvocationHandler handler) {
    return type.cast(Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { type }, handler));
  }

}
