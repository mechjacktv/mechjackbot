package tv.mechjack.mechjackbot.webapp.tempdata;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class Application {

  // USER SUPPLIED
  private static final String KEY_CLIENT_ID = "application.client_id";
  private static final String KEY_CLIENT_SECRET = "application.client_secret";
  // TWITCH SUPPLIED
  private static final String KEY_ACCESS_TOKEN = "application.access_token";
  private static final String KEY_REFRESH_TOKEN = "application.refresh_token";
  private static final String KEY_TOKEN_EXPIRES = "application.token_expires";
  private static final String KEY_TOKEN_SCOPE = "application.token_scope";
  private static final String KEY_TOKEN_TYPE = "application.token_type";

  private final Map<String, String> values = new ConcurrentHashMap<>();

  public final boolean isRegistered() {
    return Objects.nonNull(this.getClientId())
        && Objects.nonNull(this.getClientSecret());
  }

  public final boolean isExpired() {
    if (this.isRegistered()) {
      final String tokenExpires = this.getTokenExpires();

      if(Objects.nonNull(tokenExpires)) {
        final long now = System.currentTimeMillis();
        final long expires = Long.parseLong(tokenExpires);

        // If expiry in the next minute consider already expired
        return now >= expires - 60000;
      }
    }
    return true;
  }

  public final String getClientId() {
    return this.values.get(KEY_CLIENT_ID);
  }

  public final void setClientId(final String clientId) {
    this.values.put(KEY_CLIENT_ID, clientId);
  }

  public final String getClientSecret() {
    return this.values.get(KEY_CLIENT_SECRET);
  }

  public final void setClientSecret(final String clientSecret) {
    this.values.put(KEY_CLIENT_SECRET, clientSecret);
  }

  public final String getAccessToken() {
    return this.values.get(KEY_ACCESS_TOKEN);
  }

  public final void setAccessToken(final String accessToken) {
    this.values.put(KEY_ACCESS_TOKEN, accessToken);
  }

  public final String getRefreshToken() {
    return this.values.get(KEY_REFRESH_TOKEN);
  }

  public final void setRefreshToken(final String refreshToken) {
    this.values.put(KEY_REFRESH_TOKEN, refreshToken);
  }

  public final String getTokenExpires() {
    return this.values.get(KEY_TOKEN_EXPIRES);
  }

  public final void setTokenExpires(final String tokenExpires) {
    this.values.put(KEY_TOKEN_EXPIRES, tokenExpires);
  }

  public final String getTokenScope() {
    return this.values.get(KEY_TOKEN_SCOPE);
  }

  public final void setTokenScope(final String tokenScope) {
    this.values.put(KEY_TOKEN_SCOPE, tokenScope);
  }

  public final String getTokenType() {
    return this.values.get(KEY_TOKEN_TYPE);
  }

  public final void setTokenType(final String tokenType) {
    this.values.put(KEY_TOKEN_TYPE, tokenType);
  }

  public final void reset() {
    this.values.clear();
  }

}
