package tv.mechjack.platform.web.services.dispatch;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.web.services.Controller;
import tv.mechjack.platform.web.services.HttpMethod;
import tv.mechjack.platform.web.services.Service;

public final class ControllerServiceRegistry {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ControllerServiceRegistry.class);

  private Set<ServiceCommand> serviceCommands = new HashSet<>();

  @Inject
  public ControllerServiceRegistry(
      final ServiceCommandFactory serviceCommandFactory,
      final Set<Controller> controllers) {

    for (final Controller controller : controllers) {
      final Method[] methods = controller.getClass().getMethods();

      for (final Method method : methods) {
        final Service serviceAnnotation = method.getAnnotation(Service.class);

        if (Objects.nonNull(serviceAnnotation)) {
          final HttpMethod httpMethod = serviceAnnotation.method();
          final String path = serviceAnnotation.path();

          this.serviceCommands.add(serviceCommandFactory
              .createServiceCommand(controller, method, httpMethod, path));
        }
      }
    }
  }

  public boolean handleRequest(final HttpServletRequest req,
      final HttpServletResponse res) throws IOException {
    for(final ServiceCommand serviceCommand : this.serviceCommands) {
      if(serviceCommand.isHandler(req)) {
        serviceCommand.handleRequest(req, res);
        return true;
      }
    }
    return false;
  }

}
