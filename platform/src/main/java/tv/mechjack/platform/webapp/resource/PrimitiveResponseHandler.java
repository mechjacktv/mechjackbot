package tv.mechjack.platform.webapp.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.utils.typedobject.TypedBoolean;
import tv.mechjack.platform.utils.typedobject.TypedByte;
import tv.mechjack.platform.utils.typedobject.TypedCharacter;
import tv.mechjack.platform.utils.typedobject.TypedDouble;
import tv.mechjack.platform.utils.typedobject.TypedFloat;
import tv.mechjack.platform.utils.typedobject.TypedInteger;
import tv.mechjack.platform.utils.typedobject.TypedLong;
import tv.mechjack.platform.utils.typedobject.TypedShort;
import tv.mechjack.platform.utils.typedobject.TypedString;
import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.HttpStatusCode;
import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public final class PrimitiveResponseHandler implements ResponseHandler {

  @Override
  public boolean isHandler(final HttpServletRequest request,
      final Object resource) {
    // TODO (2019-03-08 mechjack): Accept Content-Type check
    return resource instanceof Boolean
        || resource instanceof TypedBoolean
        || resource instanceof Byte
        || resource instanceof TypedByte
        || resource instanceof Character
        || resource instanceof TypedCharacter
        || resource instanceof Double
        || resource instanceof TypedDouble
        || resource instanceof Float
        || resource instanceof TypedFloat
        || resource instanceof Integer
        || resource instanceof TypedInteger
        || resource instanceof Long
        || resource instanceof TypedLong
        || resource instanceof Short
        || resource instanceof TypedShort
        || resource instanceof String
        || resource instanceof TypedString;
  }

  @Override
  public void handle(final HttpServletRequest request,
      final HttpServletResponse response,
      final Object resource) throws IOException {
    response.setStatus(HttpStatusCode.OK.toInteger());
    response.setContentType(ContentType.TEXT.value);
    response.getWriter().print(resource.toString());
  }

}
