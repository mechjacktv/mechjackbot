package tv.mechjack.mechjackbot.main;

import com.google.inject.Injector;

import tv.mechjack.application.GuiceApplication;
import tv.mechjack.mechjackbot.api.ChatBot;

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
    injector.getInstance(ChatBot.class).start();
  }

}
