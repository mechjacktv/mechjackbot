package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.pircbotx.*;
import com.mechjacktv.mechjackbot.command.*;
import com.mechjacktv.mechjackbot.command.caster.*;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.guice.*;
import com.mechjacktv.mechjackbot.zero.DefaultBotConfiguration;
import com.mechjacktv.mechjackbot.zero.PropertiesAppConfiguration;
import org.pircbotx.hooks.Listener;

public class Main {

    public static void main(final String[] args) {
        System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx", "warn");
        System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx.PircBotX", "info");

        final Injector injector = Guice.createInjector(new MainModule());
        final ChatBot chatBot = injector.getInstance(ChatBot.class);

        chatBot.start();
    }

    private static class MainModule extends AbstractModule {

        @Override
        protected final void configure() {
            try {
                // Bind interceptor dependencies
                final AppConfiguration appConfiguration = new PropertiesAppConfiguration();
                final BotConfiguration botConfiguration = new DefaultBotConfiguration(appConfiguration);
                final CommandUtils commandUtils = new CommandUtils(appConfiguration, botConfiguration);

                bind(AppConfiguration.class).toInstance(appConfiguration);
                bind(BotConfiguration.class).toInstance(botConfiguration);
                bind(CommandUtils.class).toInstance(commandUtils);

                // Bind interceptors
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
                        new CommandHandleMessageMethodMatcher(),
                        new LogCommandHandleMessageMethodInterceptor());

                // Bind ChatBot
                bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();
                bind(Listener.class).to(PircBotXListener.class).asEagerSingleton();
                bind(MessageEventHandler.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();

                // Bind Commands
                bind(CasterService.class).asEagerSingleton();
                // TODO use Reflections to add bind commands
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(AddCasterCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(CasterCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(CasterListenerCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(CommandsCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(DelCasterCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(HelpCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(PingCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(TestCommand.class).asEagerSingleton();
                Multibinder.newSetBinder(binder(), Command.class).addBinding().to(QuitCommand.class).asEagerSingleton();
            } catch (final RuntimeException e) {
                throw e;
            } catch (final Exception e) {
                // TODO need a better exception
                throw new RuntimeException(e);
            }
        }

    }

}
