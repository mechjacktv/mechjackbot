package tv.mechjack.platform.webapp.resource;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.api.HttpStatusCode;
import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public final class NullResponseHandler implements ResponseHandler {

  @Override
  public boolean isHandler(final HttpServletRequest request,
      final Object resource) {
    return Objects.isNull(resource);
  }

  @Override
  public void handle(final HttpServletRequest request,
      final HttpServletResponse response,
      final Object resource) {
    response.setStatus(HttpStatusCode.NO_CONTENT.toInteger());
  }

}
