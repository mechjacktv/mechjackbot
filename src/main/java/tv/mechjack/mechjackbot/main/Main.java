package tv.mechjack.mechjackbot.main;

import com.google.inject.Injector;

import tv.mechjack.guice.GuiceApplication;
import tv.mechjack.mechjackbot.ChatBot;

final class Main extends GuiceApplication {

  public static void main(final String[] args) {
    new Main().start();
  }

  private Main() {
    super(new MainModule());
  }

  @Override
  protected void start(final Injector injector) {
    injector.getInstance(ChatBot.class).start();
  }

}
