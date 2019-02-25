package tv.mechjack.platform.web.services.dispatch;

import java.lang.reflect.Method;

import javax.inject.Inject;

import com.google.gson.Gson;

import tv.mechjack.platform.web.services.Controller;
import tv.mechjack.platform.web.services.HttpMethod;

public class ServiceCommandFactory {

  private final Gson gson;

  @Inject
  public ServiceCommandFactory(final Gson gson) {
    this.gson = gson;
  }

  final ServiceCommand createServiceCommand(final Controller controller,
      final Method javaMethod, final HttpMethod httpMethod, final String path) {
    return new ServiceCommand(this.gson, controller, javaMethod, httpMethod,
        path);
  }

}
