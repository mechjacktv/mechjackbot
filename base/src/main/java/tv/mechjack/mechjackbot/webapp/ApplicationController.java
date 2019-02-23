package tv.mechjack.mechjackbot.webapp;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.webapp.tempdata.Application;
import tv.mechjack.mechjackbot.webapp.tempdata.ApplicationCredentials;

public class ApplicationController extends BaseController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ApplicationController.class);

  private final Application application;
  private final Gson gson;

  @Inject
  ApplicationController(final Application application, final Gson gson) {
    this.application = application;
    this.gson = gson;
  }

  @Override
  public Object get(final HttpServletRequest request) {
    final Map<String, Object> response = new HashMap<>();
    final Map<String, Object> data = new HashMap<>();

    data.put("clientId", this.application.getClientId());
    data.put("token_expired", this.application.isExpired());
    response.put("data", data);
    return response;
  }

  @Override
  public Object put(final HttpServletRequest request) {
    try {
      final ApplicationCredentials credentials = this.gson
          .fromJson(request.getReader(), ApplicationCredentials.class);

      if (Objects.isNull(credentials.getClientId())) {
        return new ErrorResponse(request.getRequestURI(), "400-2",
            "clientId is required");
      } else if (Objects.isNull(credentials.getClientSecret())) {
        return new ErrorResponse(request.getRequestURI(), "400-3",
            "clientSecret is required");
      }
      this.application.setClientId(credentials.getClientId());
      this.application.setClientSecret(credentials.getClientSecret());
      return this.authenticateApplication(request);
    } catch (final IOException | InterruptedException e) {
      LOGGER.error("Unable to parse json object", e);
      return new ErrorResponse(request.getRequestURI(), "400-1",
          "Unable to parse json object");
    }
  }

  private Object authenticateApplication(final HttpServletRequest request)
      throws IOException, InterruptedException {
    final HttpClient httpClient = HttpClient.newBuilder().build();
    final URI twitchUri = URI.create(String.format(
        "https://id.twitch.tv/oauth2/token?client_id=%s"
            + "&client_secret=%s&grant_type=client_credentials"
            + "&scope=user:edit:broadcast",
        this.application.getClientId(),
        this.application.getClientSecret()));
    final HttpRequest httpRequest = HttpRequest.newBuilder().uri(twitchUri)
        .POST(BodyPublishers.noBody())
        .build();
    final HttpResponse<String> httpResponse = httpClient.send(httpRequest,
        BodyHandlers.ofString());
    final int statusCode = httpResponse.statusCode();
    final String body = httpResponse.body();

    if (statusCode < 300) {
      try (final StringReader in = new StringReader(body)) {
        final JsonReader jsonIn = this.gson.newJsonReader(in);

        jsonIn.beginObject();
        while (jsonIn.hasNext()) {
          switch (jsonIn.nextName()) {
          case "access_token":
            this.application.setAccessToken(jsonIn.nextString());
            break;
          case "refresh_token":
            this.application.setRefreshToken(jsonIn.nextString());
            break;
          case "expires_in":
            final int expiresIn = jsonIn.nextInt() * 1000;

            this.application.setTokenExpires(
                Long.toString(System.currentTimeMillis() + expiresIn));
            break;
          case "scope":
            final List<String> scopes = new ArrayList<>();

            jsonIn.beginArray();
            while (jsonIn.hasNext()) {
              scopes.add(jsonIn.nextString());
            }
            jsonIn.endArray();
            this.application.setTokenScope(String.join(", ", scopes));
            break;
          case "token_type":
            this.application.setTokenType(jsonIn.nextString());
            break;
          default:
            // ignore
            break;
          }
        }
        jsonIn.endObject();
      }
      return this.get(request);
    } else if (400 == statusCode) {
      try (final StringReader in = new StringReader(body)) {
        final JsonReader jsonIn = this.gson.newJsonReader(in);
        String status = null;
        String message = null;

        jsonIn.beginObject();
        while (jsonIn.hasNext()) {
          switch (jsonIn.nextName()) {
          case "status":
            status = jsonIn.nextString();
            break;
          case "message":
            message = jsonIn.nextString();
            break;
          default:
            // ignore
            break;
          }
        }
        jsonIn.endObject();
        return new ErrorResponse(request.getRequestURI(), status, message);
      }
    } else {
      return new ErrorResponse(request.getRequestURI(),
          Integer.toString(statusCode), body);
    }
  }

}
