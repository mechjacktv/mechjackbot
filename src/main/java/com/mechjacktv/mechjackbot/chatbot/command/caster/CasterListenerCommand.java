package com.mechjacktv.mechjackbot.chatbot.command.caster;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.chatbot.command.CommandUtils;

public class CasterListenerCommand implements Command {

    private final CasterService casterService;
    private final CommandUtils commandUtils;

    @Inject
    public CasterListenerCommand(final CasterService casterService, final CommandUtils commandUtils) {
        this.casterService = casterService;
        this.commandUtils = commandUtils;
    }

    @Override
    public final String getCommandTrigger() {
        return "!casterListener";
    }

    @Override
    public final boolean isHandledMessage(MessageEvent messageEvent) {
        return true; // take a peek at all incoming messages to see if they are from a caster
    }

    @Override
    public void handleMessage(MessageEvent messageEvent) {
        final String viewerName = this.commandUtils.getSanitizedViewerName(messageEvent);

        if(this.casterService.isCasterDue(viewerName)) {
            this.casterService.sendCasterShoutOut(messageEvent, viewerName);
        }
    }

}
