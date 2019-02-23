package tv.mechjack.mechjackbot.webapp;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@Singleton
public class DefaultJsonErrorServlet extends HttpServlet {

  private final Gson gson;

  @Inject
  public DefaultJsonErrorServlet(final Gson gson) {
    this.gson = gson;
  }

  @Override
  protected void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    resp.setStatus(Integer.parseInt(req.getAttribute(
        "javax.servlet.error.status_code").toString()));
    resp.setContentType("application/json");
    resp.getWriter().println(this.gson.toJson(new ErrorResponse(req)));
  }

}
