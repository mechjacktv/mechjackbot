package tv.mechjack.platform.webapp.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.HttpStatusCode;
import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public final class ByteArrayResponseHandler implements ResponseHandler {

  @Override
  public final boolean isHandler(final HttpServletRequest request,
      final Object resource) {
    return resource instanceof byte[];
  }

  @Override
  public final void handle(final HttpServletRequest request,
      final HttpServletResponse response,
      final Object resource) throws IOException {
    response.setStatus(HttpStatusCode.OK.toInteger());
    response.setContentType(ContentType.OCTET_STREAM.value);
    response.getOutputStream().write((byte[]) resource);
  }

}
