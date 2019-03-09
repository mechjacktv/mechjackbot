package tv.mechjack.platform.webapp.api.resource;

public abstract class BaseController implements Controller {

  private final UriPattern rootUriPattern;

  public BaseController(final UriPattern rootUriPattern) {
    this.rootUriPattern = rootUriPattern;
  }

  @Override
  public UriPattern getRootUriPattern() {
    return this.rootUriPattern;
  }

}
