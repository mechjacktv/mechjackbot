package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;

import javax.inject.Inject;

public class TestCommand  extends AbstractCommand {

    private final CommandUtils commandUtils;

    @Inject
    public TestCommand(final CommandUtils commandUtils) {
        super("!test", commandUtils);
        this.commandUtils = commandUtils;
    }

    @Override
    public final String getDescription() {
        return "A command I change freely to test development.";
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        messageEvent.sendResponse(String.format("@%s, your test completed successfully.",
                this.commandUtils.getSanitizedViewerName(messageEvent)));
    }

}
