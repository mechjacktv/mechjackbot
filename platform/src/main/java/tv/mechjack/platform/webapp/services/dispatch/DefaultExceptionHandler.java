package tv.mechjack.platform.webapp.services.dispatch;

import java.io.IOException;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.webapp.HttpStatusCode;
import tv.mechjack.platform.webapp.services.HttpServiceException;

public final class DefaultExceptionHandler implements ExceptionHandler {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultExceptionHandler.class);

  private final Gson gson;

  @Inject
  public DefaultExceptionHandler(final Gson gson) {
    this.gson = gson;
  }

  public void handle(final HttpServletRequest request,
      final HttpServletResponse response, final Exception exception)
      throws IOException {
    final StringWriter writer = new StringWriter();
    final JsonWriter jsonWriter = this.gson.newJsonWriter(response.getWriter());

    jsonWriter.beginObject();
    if (exception instanceof HttpServiceException) {
      final HttpServiceException httpServiceException = (HttpServiceException) exception;

      response.setStatus(httpServiceException.getStatusCode().toInteger());
      for (final String name : httpServiceException.getHeaderNames()) {
        response.setHeader(name,
            httpServiceException.getHeaderValue(name));
      }
      jsonWriter.name("status")
          .value(httpServiceException.getStatusCode().toInteger());
    } else {
      response.setStatus(HttpStatusCode.INTERNAL_SERVER_ERROR.toInteger());
      jsonWriter.name("status")
          .value(HttpStatusCode.INTERNAL_SERVER_ERROR.toInteger());
    }
    jsonWriter.name("resource").value(request.getRequestURI());
    jsonWriter.name("message").value(exception.getMessage());
    jsonWriter.endObject();
    response.setContentType("application/json");
    response.getWriter().print(writer.toString());
  }

}
