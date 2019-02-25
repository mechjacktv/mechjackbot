package tv.mechjack.mechjackbot.web.tempdata;

public class ApplicationCredentials {

  private final String clientId;
  private final String clientSecret;

  public ApplicationCredentials(final String clientId, final String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public final String getClientId() {
    return this.clientId;
  }

  public final String getClientSecret() {
    return this.clientSecret;
  }

}
