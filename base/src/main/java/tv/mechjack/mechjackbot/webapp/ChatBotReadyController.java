package tv.mechjack.mechjackbot.webapp;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;

public final class ChatBotReadyController extends HttpServlet {

  private final ChatBotConfiguration configuration;

  @Inject
  public ChatBotReadyController(
      final ChatBotConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    resp.setContentType("application/json");
    resp.getWriter().println("{ \"message\" : \"Hello, service!\" }");
  }

}
