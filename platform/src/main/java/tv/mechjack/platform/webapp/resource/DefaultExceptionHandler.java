package tv.mechjack.platform.webapp.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.webapp.api.HttpStatusCode;
import tv.mechjack.platform.webapp.api.exception.HttpErrorStatusException;
import tv.mechjack.platform.webapp.api.resource.ExceptionHandler;

public final class DefaultExceptionHandler
    implements ExceptionHandler {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultExceptionHandler.class);

  private final Gson gson;

  @Inject
  public DefaultExceptionHandler(final Gson gson) {
    this.gson = gson;
  }

  public final void handle(final HttpServletRequest request,
      final HttpServletResponse response, final Exception exception)
      throws IOException {

    final String errorId = UUID.randomUUID().toString();
    final StringWriter writer = new StringWriter();
    final JsonWriter jsonWriter = this.gson.newJsonWriter(response.getWriter());

    LOGGER.error(String.format("%s (%s)", exception.getMessage(), errorId),
        exception);
    jsonWriter.beginObject();
    if (exception instanceof HttpErrorStatusException) {
      final HttpErrorStatusException httpErrorStatusException = (HttpErrorStatusException) exception;

      response.setStatus(httpErrorStatusException.getStatusCode().toInteger());
      for (final String name : httpErrorStatusException.getHeaderNames()) {
        response.setHeader(name,
            httpErrorStatusException.getHeaderValue(name));
      }
      jsonWriter.name("status")
          .value(httpErrorStatusException.getStatusCode().toInteger());
    } else {
      response.setStatus(HttpStatusCode.INTERNAL_SERVER_ERROR.toInteger());
      jsonWriter.name("status")
          .value(HttpStatusCode.INTERNAL_SERVER_ERROR.toInteger());
    }
    jsonWriter.name("resource").value(request.getRequestURI());
    jsonWriter.name("message").value(exception.getMessage());
    jsonWriter.name("error_id").value(errorId);
    jsonWriter.endObject();
    response.setContentType("application/json");
    response.getWriter().print(writer.toString());
  }

}
