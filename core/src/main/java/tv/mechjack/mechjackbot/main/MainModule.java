package tv.mechjack.mechjackbot.main;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;

import com.google.inject.AbstractModule;

import tv.mechjack.mechjackbot.MechJackBotModule;
import tv.mechjack.mechjackbot.api.FeatureModule;
import tv.mechjack.platform.PlatformModule;

final class MainModule extends AbstractModule {

  private static final ServiceLoader<FeatureModule> featureServiceLoader = ServiceLoader.load(FeatureModule.class);

  public static final String KEY_EXCLUDE_FEATURE = "mechjackbot.exclude_feature";

  @Override
  protected final void configure() {
    final Set<String> excludedFeatures = this.getExcludedFeatures();
    final Iterator<FeatureModule> featureModules = featureServiceLoader.iterator();

    this.install(new PlatformModule());
    this.install(new MechJackBotModule());
    while (featureModules.hasNext()) {
      final FeatureModule featureModule = featureModules.next();

      if (!excludedFeatures.contains(featureModule.getClass().getCanonicalName())) {
        this.install(featureModule);
      }
    }
  }

  private Set<String> getExcludedFeatures() {
    final Set<String> excludedFeatures = new HashSet<>();

    Optional.ofNullable(System.getProperty(KEY_EXCLUDE_FEATURE)).ifPresent(configuredModulesString -> excludedFeatures
        .addAll(Arrays.asList(configuredModulesString.split(File.pathSeparator))));
    return excludedFeatures;
  }

}
