package tv.mechjack.platform.webapp.resource;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.HttpStatusCode;
import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public final class ObjectResponseHandler implements ResponseHandler {

  private final Gson gson;

  @Inject
  public ObjectResponseHandler(final Gson gson) {
    // TODO (2019-03-08 mechjack): Accept Content-Type check
    this.gson = gson;
  }

  @Override
  public boolean isHandler(final HttpServletRequest request,
      final Object resource) {
    return true;
  }

  @Override
  public void handle(final HttpServletRequest request,
      final HttpServletResponse response,
      final Object resource) throws IOException {
    response.setStatus(HttpStatusCode.OK.toInteger());
    response.setContentType(ContentType.JSON.value);
    response.getWriter().print(this.gson.toJson(resource));
  }

}
