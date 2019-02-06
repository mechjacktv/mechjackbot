package tv.mechjack.testframework.fake;

import java.lang.reflect.Method;

public interface Invocation {

  Object[] getArguments();

  <T> T getArgument(int index);

  Method getMethod();

  Object getFake();

}
