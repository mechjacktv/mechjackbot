package com.mechjacktv.mechjackbot.main;

import com.google.inject.AbstractModule;
import com.mechjacktv.mechjackbot.chatbot.PircBotXChatBotModule;
import com.mechjacktv.mechjackbot.command.*;
import com.mechjacktv.mechjackbot.configuration.DefaultConfigurationModule;
import com.mechjacktv.mechjackbot.keyvaluestore.MapDbKeyValueStoreModule;
import com.mechjacktv.util.DefaultUtilsModule;

final class MainModule extends AbstractModule {

    @Override
    protected final void configure() {
        install(new DefaultCommandsModule());
        install(new DefaultConfigurationModule());
        install(new MapDbKeyValueStoreModule());
        install(new PircBotXChatBotModule());
        install(new DefaultUtilsModule());
    }

}
