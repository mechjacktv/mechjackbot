package com.mechjacktv.mechjackbot.main;

import com.google.inject.Injector;

import com.mechjacktv.guice.GuiceApplication;
import com.mechjacktv.mechjackbot.ChatBot;

final class Main extends GuiceApplication {

  public static void main(final String[] args) {
    System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx", "warn");
    System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx.PircBotX", "info");
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
