package tv.mechjack.mechjackbot.api;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

public abstract class FeatureModule extends AbstractModule {

  protected final void bindCommand(
      final Class<? extends ChatCommand> chatCommandClass) {
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding()
        .to(chatCommandClass).in(Scopes.SINGLETON);
  }

}
