package tv.mechjack.mechjackbot.api;

import com.google.gson.TypeAdapter;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;
import tv.mechjack.platform.webapp.api.resource.Controller;
import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public abstract class FeatureModule extends AbstractModule {

  protected final void bindArgumentHandler(
      final Class<? extends ArgumentHandler> argumentHandler) {
    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class).addBinding()
        .to(argumentHandler)
        .in(Scopes.SINGLETON);
  }

  protected final void bindChatCommand(
      final Class<? extends ChatCommand> chatCommandClass) {
    Multibinder.newSetBinder(this.binder(), ChatCommand.class).addBinding()
        .to(chatCommandClass)
        .in(Scopes.SINGLETON);
  }

  protected final void bindController(
      final Class<? extends Controller> controller) {
    Multibinder.newSetBinder(this.binder(), Controller.class).addBinding()
        .to(controller)
        .in(Scopes.SINGLETON);
  }

  protected final void bindResponseHandler(
      final Class<? extends ResponseHandler> responseHandler) {
    Multibinder.newSetBinder(this.binder(), ResponseHandler.class).addBinding()
        .to(responseHandler)
        .in(Scopes.SINGLETON);
  }

  protected final void bindTypeAdapter(
      final Class<? extends TypeAdapter> typeAdapter) {
    Multibinder.newSetBinder(this.binder(), TypeAdapter.class).addBinding()
        .to(typeAdapter)
        .in(Scopes.SINGLETON);
  }

}
