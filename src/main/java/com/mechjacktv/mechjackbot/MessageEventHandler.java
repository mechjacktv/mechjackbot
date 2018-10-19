package com.mechjacktv.mechjackbot;

import java.util.Collection;

public interface MessageEventHandler {

    Collection<Command> getCommands();

    Command getCommand(String commandTrigger);

    void addCommand(Command command);

    void handleMessage(MessageEvent messageEvent);

}
