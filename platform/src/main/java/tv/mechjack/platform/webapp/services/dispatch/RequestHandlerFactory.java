package tv.mechjack.platform.webapp.services.dispatch;

import java.lang.reflect.Method;

import tv.mechjack.platform.webapp.services.Controller;
import tv.mechjack.platform.webapp.services.HttpMethod;
import tv.mechjack.platform.webapp.services.UriPattern;

public interface RequestHandlerFactory {

  RequestHandler createRequestHandler(Controller controller,
      Method javaMethod, ContentType contentType, HttpMethod httpMethod,
      UriPattern uriPattern);

}
