package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.chatbot.DefaultBotConfiguration;
import com.mechjacktv.mechjackbot.chatbot.PircBotXChatBot;
import com.mechjacktv.mechjackbot.chatbot.PircBotXMessageEventHandler;
import com.mechjacktv.mechjackbot.chatbot.PropertiesAppConfiguration;
import com.mechjacktv.mechjackbot.chatbot.command.*;
import com.mechjacktv.mechjackbot.chatbot.command.cooldown.GlobalCoolDown;
import com.mechjacktv.mechjackbot.chatbot.command.cooldown.GlobalCoolDownMethodInterceptor;
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
            bindInterceptor(Matchers.any(), Matchers.annotatedWith(GlobalCoolDown.class),
                    new GlobalCoolDownMethodInterceptor());

            bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();
            bind(Listener.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();
            bind(MessageEventHandler.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();
            bind(AppConfiguration.class).to(PropertiesAppConfiguration.class).asEagerSingleton();
            bind(BotConfiguration.class).to(DefaultBotConfiguration.class).asEagerSingleton();

            bind(CommandUtils.class).asEagerSingleton();
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(TestCommand.class).asEagerSingleton();
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(QuitCommand.class).asEagerSingleton();
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(PingCommand.class).asEagerSingleton();
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(ShoutOutCommand.class).asEagerSingleton();
            Multibinder.newSetBinder(binder(), Command.class).addBinding().to(SimpleCommand.class).asEagerSingleton();
        }

    }

}
