package tv.mechjack.mechjackbot.main;

import com.google.inject.Injector;

import tv.mechjack.platform.application.GuiceApplication;
import tv.mechjack.platform.webserver.WebServer;

final class Main extends GuiceApplication {

  public static final String DEFAULT_DATA_LOCATION = System.getProperty("user.home") + "/.mechjackbot";

  public static void main(final String[] args) {
    new Main().start();
  }

  private Main() {
    super(DEFAULT_DATA_LOCATION, new MainModule());
  }

  @Override
  protected void start(final Injector injector) {
    injector.getInstance(WebServer.class).start();
    // injector.getInstance(ChatBot.class).start();
  }

}
