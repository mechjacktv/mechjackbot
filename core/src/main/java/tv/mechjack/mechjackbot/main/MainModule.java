package tv.mechjack.mechjackbot.main;

import static java.lang.Class.forName;

import java.io.File;
import java.util.Optional;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.gson.GsonModule;
import tv.mechjack.keyvaluestore.KeyValueStoreModule;
import tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBotModule;
import tv.mechjack.mechjackbot.core.CoreCommandModule;
import tv.mechjack.twitchclient.TwitchClientModule;
import tv.mechjack.util.UtilModule;
import tv.mechjack.util.scheduleservice.ScheduleServiceModule;

final class MainModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainModule.class);

  public static final String KEY_PLUGIN_MODULE_NAMES = "mechjackbot.plugin_module_names";

  @Override
  protected final void configure() {
    this.install(new CoreCommandModule());
    this.install(new GsonModule());
    this.install(new KeyValueStoreModule());
    this.install(new KiclChatBotModule());
    this.install(new ScheduleServiceModule());
    this.install(new TwitchClientModule());
    this.install(new UtilModule());

    Optional.ofNullable(System.getProperty(KEY_PLUGIN_MODULE_NAMES)).ifPresent(allPluginModuleNames -> {
      final String[] pluginModuleNames = allPluginModuleNames.split(File.pathSeparator);

      for (final String pluginModuleName : pluginModuleNames) {
        try {
          this.install((Module) forName(pluginModuleName).newInstance());
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
          LOGGER.warn("Failed to load plugin module", e);
        }
      }
    });
  }

}
