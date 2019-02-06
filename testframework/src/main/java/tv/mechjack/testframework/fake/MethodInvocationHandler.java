package tv.mechjack.testframework.fake;

public interface MethodInvocationHandler {

  Object apply(Invocation invocation) throws Throwable;

}
