package com.mechjacktv.mechjackbot.chatbot.command.caster;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.chatbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.chatbot.command.CommandUtils;

public class CasterListenerCommand extends AbstractCommand {

    private final CasterService casterService;
    private final CommandUtils commandUtils;

    @Inject
    public CasterListenerCommand(final CasterService casterService, final CommandUtils commandUtils) {
        super("!casterListener", commandUtils);
        this.casterService = casterService;
        this.commandUtils = commandUtils;
    }

    @Override
    public String getDescription() {
        return "Monitors chat looking for casters who are participating.";
    }

    @Override
    public final boolean isHandledMessage(MessageEvent messageEvent) {
        return true; // take a peek at all incoming messages to see if they are from a caster
    }

    @Override
    public boolean isListed() {
        return false; // this is a passive command and can't be called
    }

    @Override
    public void handleMessage(MessageEvent messageEvent) {
        final String viewerName = this.commandUtils.getSanitizedViewerName(messageEvent);

        if(this.casterService.isCasterDue(viewerName)) {
            this.casterService.sendCasterShoutOut(messageEvent, viewerName);
        }
    }

}
