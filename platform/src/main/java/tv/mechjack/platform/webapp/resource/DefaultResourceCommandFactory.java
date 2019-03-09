package tv.mechjack.platform.webapp.resource;

import java.lang.reflect.Method;

import javax.inject.Inject;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.HttpMethod;
import tv.mechjack.platform.webapp.api.resource.Controller;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class DefaultResourceCommandFactory implements ResourceCommandFactory {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultResourceCommandFactory.class);

  private final ArgumentHandlerRegistry argumentHandlerRegistry;
  private final Gson gson;
  private final ResourceCommandComparator resourceCommandComparator;
  private final ResouceCommandMatcher resouceCommandMatcher;

  @Inject
  public DefaultResourceCommandFactory(
      final ArgumentHandlerRegistry argumentHandlerRegistry,
      final Gson gson, final ResourceCommandComparator resourceCommandComparator,
      final ResouceCommandMatcher resouceCommandMatcher) {
    this.argumentHandlerRegistry = argumentHandlerRegistry;
    this.gson = gson;
    this.resourceCommandComparator = resourceCommandComparator;
    this.resouceCommandMatcher = resouceCommandMatcher;
  }

  @Override
  public final ResourceCommand createRequestHandler(final Controller controller,
      final Method handler, final ContentType contentType,
      final HttpMethod httpMethod, final UriPattern uriPattern) {
    return new DefaultResourceCommand(this.argumentHandlerRegistry, this.gson,
        this.resourceCommandComparator, this.resouceCommandMatcher,
        controller,
        handler, contentType,
        httpMethod, uriPattern);
  }

}
