package tv.mechjack.mechjackbot.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class WaitServlet extends HttpServlet {

  @Override
  protected void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    try {
      Thread.sleep(Long.parseLong(req.getParameter("time")));
    } catch (final InterruptedException e) {
      // ignore
    }
    resp.setStatus(204);
  }

}
