package tv.mechjack.testframework.fake;

import java.lang.reflect.InvocationHandler;

public interface FakeFactory {

  <T> FakeBuilder<T> builder(Class<T> type);

  <T> T fake(Class<T> type);

  <T> T fake(Class<T> type, InvocationHandler handler);

}
