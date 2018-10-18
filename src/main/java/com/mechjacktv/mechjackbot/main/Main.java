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
import com.mechjacktv.mechjackbot.chatbot.command.log.LogCommandHandleMessageMethodInterceptor;
import com.mechjacktv.mechjackbot.chatbot.command.log.LogCommandHandleMessageMethodMatcher;
import com.mechjacktv.mechjackbot.chatbot.command.restrict.*;
import org.pircbotx.hooks.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    public static void main(final String[] args) {
        System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx", "warn");
        System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx.PircBotX", "info");
//        System.setProperty("org.slf4j.simpleLogger.log.com.mechjacktv.mechjackbot", "info");

        final Injector injector = Guice.createInjector(new MainModule());
        final ChatBot chatBot = injector.getInstance(ChatBot.class);

        chatBot.start();
    }

    private static class MainModule extends AbstractModule {

        @Override
        protected final void configure() {
            try {
                final AppConfiguration appConfiguration = new PropertiesAppConfiguration();
                final BotConfiguration botConfiguration = new DefaultBotConfiguration(appConfiguration);
                final CommandUtils commandUtils = new CommandUtils(appConfiguration, botConfiguration);

                bind(AppConfiguration.class).toInstance(appConfiguration);
                bind(BotConfiguration.class).toInstance(botConfiguration);
                bind(CommandUtils.class).toInstance(commandUtils);

                bindInterceptor(Matchers.subclassesOf(Command.class),
                        Matchers.annotatedWith(RestrictToOwner.class),
                        new RestrictToOwnerMethodInterceptor(commandUtils));
                bindInterceptor(Matchers.subclassesOf(Command.class),
                        Matchers.annotatedWith(RestrictToPrivileged.class),
                        new RestrictToPrivilegedMethodInterceptor(commandUtils));
                bindInterceptor(Matchers.subclassesOf(Command.class),
                        Matchers.annotatedWith(RestrictToRegular.class),
                        new RestrictToRegularMethodInterceptor(commandUtils));
                bindInterceptor(Matchers.subclassesOf(Command.class),
                        Matchers.annotatedWith(GlobalCoolDown.class),
                        new GlobalCoolDownMethodInterceptor(commandUtils));
                bindInterceptor(Matchers.subclassesOf(Command.class),
                        new LogCommandHandleMessageMethodMatcher(),
                        new LogCommandHandleMessageMethodInterceptor());

                bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();
                bind(Listener.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();
                bind(MessageEventHandler.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();

                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(CasterCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(PingCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(TestCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(QuitCommand.class).asEagerSingleton();
//                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(ShoutOutCommand.class).asEagerSingleton();
//                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(SimpleCommand.class).asEagerSingleton();
            } catch (final RuntimeException e) {
                throw e;
            } catch (final Exception e) {
                // TODO need a better exception
                throw new RuntimeException(e);
            }
        }

    }

}
