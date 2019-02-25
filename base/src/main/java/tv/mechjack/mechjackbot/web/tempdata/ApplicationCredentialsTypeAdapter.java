package tv.mechjack.mechjackbot.web.tempdata;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ApplicationCredentialsTypeAdapter
    extends TypeAdapter<ApplicationCredentials> {

  @Override
  public void write(final JsonWriter out,
      final ApplicationCredentials applicationCredentials)
      throws IOException {
    out.beginObject();
    out.name("clientId").value(applicationCredentials.getClientId());
    out.name("clientSecret").value(applicationCredentials.getClientSecret());
    out.endObject();
  }

  @Override
  public ApplicationCredentials read(final JsonReader in) throws IOException {
    String clientId = null;
    String clientSecret = null;

    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
      case "clientId":
        clientId = in.nextString();
        break;
      case "clientSecret":
        clientSecret = in.nextString();
        break;
      default:
        // ignore
        break;
      }
    }
    in.endObject();
    return new ApplicationCredentials(clientId, clientSecret);
  }

}
