package tv.mechjack.platform.webapp.services.dispatch;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import tv.mechjack.platform.webapp.services.HttpMethod;
import tv.mechjack.platform.webapp.services.UriPattern;
import tv.mechjack.platform.webapp.services.dispatch.RequestHandler.Reason;

public interface RequestHandlerMatcher {

  boolean isHandler(HttpServletRequest request, ContentType contentType,
      HttpMethod httpMethod, UriPattern uriPattern,
      Consumer<Reason> observer);

}
