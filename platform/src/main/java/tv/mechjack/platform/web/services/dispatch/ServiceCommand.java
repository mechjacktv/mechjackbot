package tv.mechjack.platform.web.services.dispatch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.web.services.Controller;
import tv.mechjack.platform.web.services.HttpMethod;

final class ServiceCommand {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ServiceCommand.class);

  private final Gson gson;

  private final Controller controller;

  private final Method javaMethod;

  private final HttpMethod httpMethod;

  private final String path;

  ServiceCommand(final Gson gson, final Controller controller,
      final Method javaMethod, final HttpMethod httpMethod, final String path) {
    this.gson = gson;
    this.controller = controller;
    this.javaMethod = javaMethod;
    this.httpMethod = httpMethod;
    this.path = path;
  }

  public final boolean isHandler(final HttpServletRequest req) {
    final HttpMethod reqHttpMethod = HttpMethod.valueOf(req.getMethod().toUpperCase());
    // TODO (2019-02-24 mechjack): remove context path
    final String reqRequestURI = req.getRequestURI();

    return this.httpMethod.equals(reqHttpMethod)
        && this.path.equalsIgnoreCase(reqRequestURI);
  }

  public final void handleRequest(final HttpServletRequest req,
      final HttpServletResponse res) throws IOException {
    try {
      final Object[] arguments = this.getArguments(this.javaMethod, req, res);
      final Object response = this.javaMethod
          .invoke(this.controller, arguments);

      if (Objects.nonNull(response)) {
        res.setContentType("application/json");
        res.getWriter().print(this.gson.toJson(response));
      }
    } catch (final IllegalAccessException | InvocationTargetException | IOException e) {
      LOGGER.error("Failure while executing service" + e.getMessage(), e);
      res.setStatus(500);
      res.setContentType("text/plain");
      res.getWriter().print(e.getMessage());
    }
  }

  private Object[] getArguments(final Method javaMethod,
      final HttpServletRequest req, final HttpServletResponse res)
      throws IOException {
    final List<Object> arguments = new ArrayList<>();
    for (final Class<?> parameter : javaMethod.getParameterTypes()) {
      if (HttpServletRequest.class.isAssignableFrom(parameter)) {
        arguments.add(req);
      } else if (HttpServletResponse.class.isAssignableFrom(parameter)) {
        arguments.add(res);
      } else {
        arguments.add(this.gson.fromJson(req.getReader(), parameter));
      }
    }
    return arguments.toArray();
  }

}
