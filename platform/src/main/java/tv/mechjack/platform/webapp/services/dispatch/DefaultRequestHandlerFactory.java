package tv.mechjack.platform.webapp.services.dispatch;

import java.lang.reflect.Method;

import javax.inject.Inject;

import com.google.gson.Gson;

import tv.mechjack.platform.webapp.services.Controller;
import tv.mechjack.platform.webapp.services.HttpMethod;
import tv.mechjack.platform.webapp.services.UriPattern;

public class DefaultRequestHandlerFactory implements RequestHandlerFactory {

  private final ArgumentHandlerRegistry argumentHandlerRegistry;
  private final Gson gson;
  private final RequestHandlerMatcher requestHandlerMatcher;

  @Inject
  public DefaultRequestHandlerFactory(final ArgumentHandlerRegistry argumentHandlerRegistry,
      final Gson gson, final RequestHandlerMatcher requestHandlerMatcher) {
    this.argumentHandlerRegistry = argumentHandlerRegistry;
    this.gson = gson;
    this.requestHandlerMatcher = requestHandlerMatcher;
  }

  @Override
  public final RequestHandler createRequestHandler(final Controller controller,
      final Method javaMethod, final ContentType contentType,
      final HttpMethod httpMethod, final UriPattern uriPattern) {
    return new DefaultRequestHandler(this.argumentHandlerRegistry, this.gson,
        this.requestHandlerMatcher, controller, javaMethod, contentType,
        httpMethod, uriPattern);
  }

}
