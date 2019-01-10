package tv.mechjack.mechjackbot.command;

import java.util.Collection;

import picocli.CommandLine.IParseResultHandler2;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Model.CommandSpec;

import tv.mechjack.mechjackbot.ChatMessageEvent;

public interface PicoCliCommandParser {

  boolean parseArguments(Collection<ArgSpec> argSpecs, ChatMessageEvent messageEvent,
      IParseResultHandler2<Boolean> handler);

  boolean parseArguments(CommandSpec commandSpec, ChatMessageEvent messageEvent, IParseResultHandler2<Boolean> handler);

}
