package tv.mechjack.platform.web;

public interface WebApplication {

  String getContextPath();

  String getResourceBase();

  void registerControllers(ControllerHandler controllerHandler);

  void registerErrorPages(ErrorPageHandler errorPageHandler);

}
