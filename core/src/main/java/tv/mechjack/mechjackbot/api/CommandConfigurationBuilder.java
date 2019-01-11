package tv.mechjack.mechjackbot.api;

public interface CommandConfigurationBuilder {

  CommandConfigurationBuilder setDescription(String description);

  CommandConfigurationBuilder setMessageFormat(String messageFormat);

  CommandConfigurationBuilder setTrigger(String trigger);

  CommandConfigurationBuilder setTriggerable(boolean triggerable);

  CommandConfigurationBuilder setUsage(String usage);

  CommandConfiguration build();

}
