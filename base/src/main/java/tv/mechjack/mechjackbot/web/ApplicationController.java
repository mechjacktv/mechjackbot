package tv.mechjack.mechjackbot.web;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.web.tempdata.Application;
import tv.mechjack.platform.webapp.api.HttpMethod;
import tv.mechjack.platform.webapp.api.resource.BaseController;
import tv.mechjack.platform.webapp.api.resource.RequestHandler;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class ApplicationController extends BaseController {

  private final Application application;

  @Inject
  public ApplicationController(final Application application) {
    super(UriPattern.of("/application"));
    this.application = application;
  }

  @RequestHandler(method = HttpMethod.GET)
  public final ApplicationRepresentation getApplication() {
    return new ApplicationRepresentation(this.application);
  }

  public static final class ApplicationRepresentation {

    private final String clientId;
    private final boolean registered;
    private final boolean tokenExpired;

    public ApplicationRepresentation(final Application application) {
      this.clientId = application.getClientId();
      this.registered = application.isRegistered();
      this.tokenExpired = application.isExpired();
    }

  }

}
