package tv.mechjack.mechjackbot.webapp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@Singleton
public class ApplicationServlet extends HttpServlet {

  private final Map<String, String> properties = new HashMap();
  private final Gson gson;

  @Inject
  public ApplicationServlet(final Gson gson) {
    this.gson = gson;
  }

  @Override
  protected void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    final StringBuilder builder = new StringBuilder(
        "{ \"data\": {\"ready\": false");

    if (this.properties.containsKey("clientId")) {
      builder.append(", \"clientId\" : \"").append(this.properties.get(
          "clientId")).append("\"");
    }
    if (this.properties.containsKey("token_type")) {
      builder.append(", \"token_type\" : \"").append(this.properties.get(
          "token_type")).append("\"");
    }
    builder.append(" } }");
    resp.setContentType("application/json");
    resp.getWriter().println(builder.toString());
  }

  @Override
  protected void doPut(final HttpServletRequest req,
      final HttpServletResponse resp) throws IOException {
    final JsonReader jsonReader = this.gson.newJsonReader(req.getReader());

    jsonReader.beginObject();
    while (jsonReader.hasNext()) {
      this.properties.put(jsonReader.nextName(), jsonReader.nextString());
    }
    jsonReader.endObject();
    this.doGet(req, resp);
  }

}
