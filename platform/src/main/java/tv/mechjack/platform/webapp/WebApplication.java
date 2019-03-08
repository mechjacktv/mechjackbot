package tv.mechjack.platform.webapp;

public interface WebApplication {

  String getContextPath();

  String getResourceBase();

  void registerControllers(ControllerHandler controllerHandler);

  void registerErrorPages(ErrorPageHandler errorPageHandler);

}
