package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class TestCommand  extends AbstractCommand {

    @Inject
    public TestCommand(final CommandUtils commandUtils) {
        super("!test", commandUtils);
    }

    @Override
    public final String getDescription() {
        return "A command I change freely to test development.";
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        messageEvent.sendResponse("Does nothing");
    }

}
