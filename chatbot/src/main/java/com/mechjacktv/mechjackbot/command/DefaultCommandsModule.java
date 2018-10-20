package com.mechjacktv.mechjackbot.command;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.command.caster.*;
import com.mechjacktv.mechjackbot.command.interceptor.DefaultCommandInterceptorsModule;

public final class DefaultCommandsModule extends AbstractModule {

    @Override
    protected final void configure() {
        install(new DefaultCommandInterceptorsModule());

        bind(CommandUtils.class).asEagerSingleton();
        bind(CasterService.class).asEagerSingleton();
        // TODO automate
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(AddCasterCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(CasterCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(CasterListenerCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(CommandsCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(DelCasterCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(HelpCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(PingCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(TestCommand.class).asEagerSingleton();
        Multibinder.newSetBinder(binder(), Command.class).addBinding().to(QuitCommand.class).asEagerSingleton();
    }

}
