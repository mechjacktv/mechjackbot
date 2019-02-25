package tv.mechjack.mechjackbot.web;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.web.tempdata.Application;
import tv.mechjack.platform.web.services.Controller;
import tv.mechjack.platform.web.services.HttpMethod;
import tv.mechjack.platform.web.services.Service;

public final class ApplicationController implements Controller {

  private final Application application;

  @Inject
  public ApplicationController(final Application application) {
    this.application = application;
  }

  @Service(method = HttpMethod.GET, path="/api/v1/application")
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
