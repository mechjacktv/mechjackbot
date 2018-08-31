package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.chatbot.PircBotXChatBot;
import com.mechjacktv.mechjackbot.chatbot.command.CommandUtils;
import com.mechjacktv.mechjackbot.chatbot.command.PingCommand;
import com.mechjacktv.mechjackbot.chatbot.command.QuitCommand;
import com.mechjacktv.mechjackbot.chatbot.DefaultBotConfiguration;
import com.mechjacktv.mechjackbot.chatbot.PropertiesAppConfiguration;
import com.mechjacktv.mechjackbot.chatbot.PircBotXMessageEventHandler;
import com.mechjacktv.mechjackbot.chatbot.command.ShoutOutCommand;
import com.mechjacktv.mechjackbot.interceptor.CooldownInterceptor;
import org.pircbotx.hooks.Listener;

public class Main {

    public static void main(final String[] args) {

        final Injector injector = Guice.createInjector(new MainModule());
        final ChatBot chatBot = injector.getInstance(ChatBot.class);

        chatBot.start();
    }

    private static class MainModule extends AbstractModule {

        @Override
        protected final void configure() {
            bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();
            bind(Listener.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();
            bind(MessageEventHandler.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();
            bind(AppConfiguration.class).to(PropertiesAppConfiguration.class).asEagerSingleton();
            bind(BotConfiguration.class).to(DefaultBotConfiguration.class).asEagerSingleton();

            bind(CommandUtils.class).asEagerSingleton();
            bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cooldown.class),
                    new CooldownInterceptor());
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(PingCommand.class).asEagerSingleton();
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(ShoutOutCommand.class).asEagerSingleton();
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(QuitCommand.class).asEagerSingleton();
        }

    }

}
