package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.*;
import com.mechjacktv.mechjackbot.command.interceptor.CommandInterceptorsModule;
import com.mechjacktv.mechjackbot.chatbot.PircBotXChatBot;
import com.mechjacktv.mechjackbot.chatbot.PircBotXListener;
import com.mechjacktv.mechjackbot.chatbot.PircBotXMessageEventHandler;
import com.mechjacktv.mechjackbot.configuration.DefaultBotConfiguration;
import com.mechjacktv.mechjackbot.configuration.PropertiesAppConfiguration;
import com.mechjacktv.util.UtilsModule;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.pircbotx.hooks.Listener;

class MainModule extends AbstractModule {

    private static final String MAP_DB_LOCATION = System.getProperty("user.home") + "/.mechjackbot.db";

    @Override
    protected final void configure() {
        install(new CommandInterceptorsModule());
        install(new CommandsModule());
        install(new UtilsModule());

        // Bind MapDB
        bind(DB.class).toInstance(DBMaker.fileDB(MAP_DB_LOCATION).make());

        // Bind ChatBot
        bind(AppConfiguration.class).to(PropertiesAppConfiguration.class).asEagerSingleton();
        bind(BotConfiguration.class).to(DefaultBotConfiguration.class).asEagerSingleton();
        bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();
        bind(Listener.class).to(PircBotXListener.class).asEagerSingleton();
        bind(MessageEventHandler.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();
    }

}
