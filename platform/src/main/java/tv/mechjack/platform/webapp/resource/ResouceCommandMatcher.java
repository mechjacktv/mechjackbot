package tv.mechjack.platform.webapp.resource;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.HttpMethod;
import tv.mechjack.platform.webapp.api.resource.UriPattern;
import tv.mechjack.platform.webapp.resource.ResourceCommand.Reason;

public interface ResouceCommandMatcher {

  boolean isHandler(HttpServletRequest request, ContentType contentType,
      HttpMethod httpMethod, UriPattern uriPattern,
      Consumer<Reason> observer);

}
