package tv.mechjack.mechjackbot.webapp;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ControllerServlet extends HttpServlet {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ControllerServlet.class);

  public final Gson gson;

  public final Controller controller;

  ControllerServlet(final Gson gson, final Controller controller) {
    this.gson = gson;
    this.controller = controller;
  }

  @Override
  protected void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    this.doCall(req, resp, this.controller::get);
  }

  @Override
  protected void doPost(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    this.doCall(req, resp, this.controller::post);
  }

  @Override
  protected void doPut(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    this.doCall(req, resp, this.controller::put);
  }

  @Override
  protected void doDelete(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    this.doCall(req, resp, this.controller::put);
  }

  private void doCall(final HttpServletRequest req,
      final HttpServletResponse resp,
      final Function<HttpServletRequest, Object> handler) throws IOException {
    try {
      final Object response = handler.apply(req);

      if (Objects.nonNull(response)) {
        resp.setContentType("application/json");
        if (response instanceof ErrorResponse) {
          final String status = ((ErrorResponse) response).getStatus();
          final String[] statusParts = status.split("-");

          resp.setStatus(Integer.parseInt(statusParts[0]));
        }
        resp.getWriter().println(this.gson.toJson(response));
      } else {
        resp.setStatus(204);
      }
    } catch (final RuntimeException error) {
      LOGGER.error(error.getMessage(), error);
      resp.setContentType("application/json");
      resp.getWriter().println(this.gson.toJson(
          new ErrorResponse(req.getRequestURI(), "500", error.getMessage())));
    }
  }

}
