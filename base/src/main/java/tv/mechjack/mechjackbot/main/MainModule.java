package tv.mechjack.mechjackbot.main;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;

import com.google.inject.AbstractModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.FeatureModule;
import tv.mechjack.mechjackbot.base.BaseModule;
import tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBotModule;
import tv.mechjack.mechjackbot.webapp.WebApplicationModule;
import tv.mechjack.platform.PlatformModule;
import tv.mechjack.platform.protobuf.ProtobufModule;
import tv.mechjack.twitchclient.TwitchClientModule;

final class MainModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MainModule.class);

  private static final ServiceLoader<FeatureModule> featureServiceLoader = ServiceLoader
      .load(FeatureModule.class);

  public static final String KEY_EXCLUDE_FEATURE = "mechjackbot.exclude_feature";

  @Override
  protected final void configure() {
    final Set<String> excludedFeatures = this.getExcludedFeatures();
    final Iterator<FeatureModule> featureModules = featureServiceLoader
        .iterator();

    this.install(new BaseModule());
    this.install(new KiclChatBotModule());
    this.install(new PlatformModule());
    this.install(new ProtobufModule());
    this.install(new TwitchClientModule());
    this.install(new WebApplicationModule());
    while (featureModules.hasNext()) {
      final FeatureModule featureModule = featureModules.next();

      if (!excludedFeatures
          .contains(featureModule.getClass().getCanonicalName())) {
        LOGGER.info("Loading Feature: "
            + featureModule.getClass().getCanonicalName());
        this.install(featureModule);
      }
    }
  }

  private Set<String> getExcludedFeatures() {
    final Set<String> excludedFeatures = new HashSet<>();

    Optional.ofNullable(System.getProperty(KEY_EXCLUDE_FEATURE))
        .ifPresent(configuredModulesString -> excludedFeatures
            .addAll(Arrays
                .asList(configuredModulesString.split(File.pathSeparator))));
    return excludedFeatures;
  }

}
