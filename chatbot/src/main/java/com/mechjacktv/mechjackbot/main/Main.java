package com.mechjacktv.mechjackbot.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mechjacktv.mechjackbot.*;

public final class Main {

    public final static void main(final String[] args) {
        // TODO make logging great again
        System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx", "warn");
        System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx.PircBotX", "info");

        final Injector injector = Guice.createInjector(new MainModule());
        final ChatBot chatBot = injector.getInstance(ChatBot.class);

        chatBot.start();
    }

}
