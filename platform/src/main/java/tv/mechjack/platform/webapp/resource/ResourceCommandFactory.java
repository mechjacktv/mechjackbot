package tv.mechjack.platform.webapp.resource;

import java.lang.reflect.Method;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.HttpMethod;
import tv.mechjack.platform.webapp.api.resource.Controller;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public interface ResourceCommandFactory {

  ResourceCommand createRequestHandler(Controller controller,
      Method javaMethod, ContentType contentType, HttpMethod httpMethod,
      UriPattern uriPattern);

}
