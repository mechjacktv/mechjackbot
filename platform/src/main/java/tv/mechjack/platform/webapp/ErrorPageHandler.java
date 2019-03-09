package tv.mechjack.platform.webapp;

public interface ErrorPageHandler {

  void registerErrorPage(int code, String uri);

  void registerErrorPage(int from, int to, String uri);

  void registerErrorPage(Class<Throwable> throwableClass, String uri);

}