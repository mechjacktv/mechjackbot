# Installing MechJackBot

## Introduction

This document provides instructions on how to install MechJackBot on your OS X or Windows computer. Linux is going to be pretty similar, but with an opportunity for the reader to figure out the details. This document is a work in progress and any questions or feedback you have about the process described in this document is greatly appreciated.

MechJackBot *should* work on any computer with Java 8 or later, but for testing we used OS X Mojave (with both Java 8

Eventually we'll be able to start MechJackBot at this point and configuration will be done via a web-based user interface, but we're not there yet.and Java 11) and Windows 10 (with Java 8).

### Warning About Pre-Releases, Versions, and Backwards Compatibility

MechJackBot is in pre-release and changes may break backward compatibility with previous releases until version `1.0.0` is released. You are highly encouraged to check this document each time you take a new release to see what has changed.

Once version `1.0.0` is released if a change breaks backwards compatibility you'll know because the first number in the version will be incremented. For example you should be able to upgrade from any `1.x.x` release to any other *later* `1.x.x` release. The first backwards incompatible release for `1.x.x` releases would be `2.0.0`.

## Download and Install Java

MechJackBot is written in Java and requires a Java Runtime Environment to run. In particular it needs version 8 or higher. If you want to check which version of Java you have on your computer, open `Terminal` on OS X or `Command Prompt` on Windows and type `java -version`. You will see output like the following if you have Java installed:

```
java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)
```

If it's installed you're looking for a `java version` that looks like `1.8.X_XXX` or `11.0.X`. If you're not sure if you have Java or what version you have, it generally won't hurt to download and try to install the latest Java version on your computer.

You can download the latest version of Java at [https://www.java.com/en/download/](https://www.java.com/en/download/) (the version was Version 8 Update 191 at the time of writing). Once you've downloaded it execute the installer.

## Download and Install MechJackBot

You can find the latest release of MechJackBot at [https://github.com/mechjacktv/mechjackbot/releases](https://github.com/mechjacktv/mechjackbot/releases). You can download either the `.tar` or `.zip` file as they contain the same thing. If you're not sure which, download the `.zip` file.

Move the file you downloaded from your downloads location to where you want the MechJackBot code to live and then extract the file. For `.zip` files on OS X and Windows double-click the file. On OS X the extracted files will show up as a folder in the same directory. On Windows click `Extract All` and it will prompt you where to extract the files with the default being a folder in the same directory so all you have to do is click `Extract`.

*Remember this location because we're going to come back here later.*

If you selected the `.tar` file it's assumed you know what you're doing and we won't cover it here.

## Configure MechJackBot

Eventually we'll be able to start MechJackBot at this point and configuration you'll configure via a web-based user interface, but we're not there yet. For now we need to create configuration files by hand.

By default MechJackBot will look for configuration data in a folder called `.mechjackbot` located in your home directory. For OS X this will be something like `/Users/username/.mechjackbot`. On Windows this will be something like `C:\Users\username\.mechjackbot`.

Once you've created this folder we need to create two files before we can start up MechJackBot. The first file is called `chat_bot.config` and the second is called `application.config`.

### chat_bot.config

This file is for login information that MechJackBot will use to connect to your chat and use the Twitch APIs. Right now we need four pieces of information (there will be more in the future as we add features to MechJackBot).

```
twitch.channel = yourtwitchlogin
twitch.login = twitchloginforbot
twitch.password = oauth:secretlettersandnumbers
twitch.client_id = differentsecretlettersandnumbers
```

MechJackBot uses the first three values to connect to your chat. You can either use your own Twitch account to login or [create a new one](https://www.twitch.tv/signup) specifically for your bot. Either way, once you have an account to use for your bot, go to [https://twitchapps.com/tmi/](https://twitchapps.com/tmi/) and follow the directions to get an `oauth token` to use as your password. Make sure to keep the `oauth:` at the start of the password.

If you created a new account for your bot, you will probably want to *mod* that account in your channel once the bot has connected and is running.

MechJackBot uses the last value to call the Twitch APIs. You'll need to create a Twitch Developer account to get a new Client Id. Start the process by going to [https://dev.twitch.tv/](https://dev.twitch.tv/) and singing in by connecting your Twitch Account.

Once you are logged in, you'll see a link called `Your Dashboard` at the top right of the page. This is your developer dashboard and not your regular Twitch dashboard. Click it to go to your developer dashboard. Once there, click `Apps`. No we can register your application to get a new Client Id.

Click `Register Your Application` on the right of the page. Twitch wants three pieces of information about your bot. The first is it's name. We suggest you use your twitch login followed by `_bot`. What's important is that it's unique. The site will tell you if the name you've chosen is already in use. We don't make use of the OAuth redirect URL yet, so you can just put `http://localhost`. Finally, for Category select `Chat Bot`.

You'll now see your new bot application listed on your `Apps` page. Click `Manage` to get your Client Id. Someday we'll need the Client Secret as well, but not yet. This is the place you'll go to get it once we do.

### application.config

This is where you'll put any configurations for MechJackBot when you aren't satisfied with the defaults. For now leave the file empty. Eventually this will be handled via a web-based user interface as well, but we're not there yet either.

## Start MechJackBot

To start MechJackBot you need to execute a startup script that will handle all the details for you. There is one for OS X/Linux and one for Windows and they are located in the `bin` directory of the folder where you extracted MechJackBot.

They are currently called `all` (OS X/Linux) and `all.bat` (Windows). They will eventually be renamed to `mechjackbot`. Double click the one appropriate for your platform. Alternatively, you can run them from `Terminal` or the `Command Prompt` respectivly.

### OS X

The first time you run MechJackBot you'll be prompted that this is an application from an uncertified developer. You'll need to go into `System Preferences` > `Security & Privacy` > `General` and select `Open Anyway`. If you run it from `Terminal` you won't need to do this.

### Windows

The first time you run MechJackBot you'll get a prompt that Windows Defender has protected your computer. Click `More info` and then `Run`. If you run it from the `Command Prompt` you won't need to do this.

## Smoke Test MechJackBot

Give MechJackBot a few seconds to start and then check that it's running by typing `!commands` in your chat. You should see your bot reply with `MrDestructoid > Commands: !commands !delcommand !help !ping !quit !setcommand`.

If you see that you're good to go. If you can get support on Discord at [https://discord.gg/TbrF6g](https://discord.gg/TbrF6g), via email at [mechjacktv@gmail.com](mailto:mechjacktv@gmail.com) or by opening up a new issue on GitHub at [https://github.com/mechjacktv/mechjackbot/issues](https://github.com/mechjacktv/mechjackbot/issues).

## Optional Configuration

There are a number of configurable properties with what we hope are reasonable defaults. You can change the defaults in the `application.config` file we created earlier. Each configuration property fits the same `<name> = <value>` format like in `chat_bot.config`.

For any output sent to chat you can include `$(user)` and the calling user login will be substituted or `$(trigger)` and the command's trigger will be substituted.

### Non-command Configuration Properties

|Property Name|Default Value|Notes|
|--|--|--|
|chat_bot.message_format|/me MrDestructoid > %s|It's important that `%s` show up somewhere and exactly once. It will be replaced by the body of the message.|
|chat_bot.shutdown.message|Shutdown|This is the message that MechJackBot will send the IRC server when shutting down. Nobody sees it and it doesn't really matter.|
|command.command_cool_down.seconds|5|This is the number of seconds before the command can be triggered by anybody (broadcaster excluded).|
|command.viewer_cool_down.seconds|15|This is the number of seconds before a viewer can issue another command (broadcaster excluded).|
|command.usage_message_format|@$(user), usage: $(trigger) %s|This is the message format used when incorrect arguments are passed to a command.|

There are 7 commands included with MechJackBot right now. The name of a config property for commands uses the format of `<scope>.<name>`. Each command description below will give you the scope and available config options.

#### Reading Usage

The usage for each command will start with the default trigger and then a description of the commands arguments.

Things wrapped in `<` and `>` denote values that should be entered. For example the usage for the help command is `!help <trigger>` but to call it to get the description of the quit command you would type `!help !quit` in chat.

Things wrapped in `[` and `]` denote values that are optional. Sometimes they are only conditionally optional and that will be noted.

Things wrapped in `(` and `)` denote a list of values that can be selected from separated by the `|` character.

### Core Commands

#### Commands

* Scope: `tv_mechjack_mechjackbot_core_commandschatcommand`.
* Usage: `!commands`

|Property Name|Default Value|Notes|
|--|--|--|
|description|Lists all the commands available to users.|You can change this to whatever you want, but generally it should be helpful to viewers.|
|message_format|Commands: %s|`%s` will be substituted with the response body.|
|trigger|!commands|This is what a message must start with to trigger the command.|

#### Help

* Scope: `tv_mechjack_mechjackbot_core_helpchatcommand`
* Usage: `!help <trigger>`

|Property Name|Default Value|Notes|
|--|--|--|
|description|Returns the description for a command.|You can change this to whatever you want, but generally it should be helpful to viewers.|
|message_format|@$(user), %s -> %s|The first `%s` will be substituted with the command trigger help was requested for and the second with the description of the command for that trigger.|
|missing_message_format|@$(user), I don't see a command triggered by %s.|This is the message sent if the help command is requested for a command trigger that doesn't exist. The `%s` will be substituted with the command trigger help was requested for.|
|trigger|!help|This is what a message must start with to trigger the command.|

#### Ping

* Scope: `tv_mechjack_mechjackbot_core_pingchatcommand`.
* Usage: `!ping`

|Property Name|Default Value|Notes|
|--|--|--|
|description|A simple check to see if the chat bot is running.|You can change this to whatever you want, but generally it should be helpful to viewers.|
|message_format|Don't worry, @$(user). I'm here.|You can change this to whatever you want.|
|trigger|!ping|This is what a message must start with to trigger the command.|

#### Quit

* Scope: `tv_mechjack_mechjackbot_core_quitchatcommand`.
* Usage: `!quit`

|Property Name|Default Value|Notes|
|--|--|--|
|description|Shuts the chat bot down.|You can change this to whatever you want, but generally it should be helpful to viewers.|
|message_format|Powering down|This will be sent to chat before the bot shutsdown. You can change this to whatever you want.|
|trigger|!quit|This is what a message must start with to trigger the command.|

### Custom Commands

#### Delete Command

* Scope: `tv_mechjack_mechjackbot_command_custom_deletecommandchatcommand`.
* Usage: `!delcommand <trigger>`

|Property Name|Default Value|Notes|
|--|--|--|
|description|Remove a custom command.|You can change this to whatever you want, but generally it should be helpful to viewers.|
|message_format|$(user), command removed, %s|`%s` will be substituted with the custom command trigger.|
|not_custom_command_message_format|$(user), %s is not a custom command|This is the message sent if no custom command for the specified trigger exists.|
|trigger|!delcommand|This is what a message must start with to trigger the command.|

#### Set Command

* Scope: `tv_mechjack_mechjackbot_command_custom_setcommandchatcommand`.
* Usage: `!setcommand <trigger> [(-r|--user-role)=(BROADCASTER|MODERATOR|VIP|SUBSCRIBER|VIEWER)] [(-d|--description)=<description>] [<body>]`
    * Right now all user roles other than `VIEWER` roll up into `BROADCASTER`. The other levels haven't been implemented yet.
    * The command reports `-u|--user-role` in the usage. That is an error. It's really `-r|--user-role`.
    * The `<body>` is only optional if you are modifying an existing command and not creating a new one.
    * The command body may optionally include named arguments in the format `${<name>}`. If the same name is used twice that argument value will be repeated in both places. The command arguments will be interpreted in the order the names occur in the body (ignoring duplicate names). For example if we had a `!hug` command with the body `$(user) hugs ${target}!` the usage for `!hug` would be `!hug <target>` and you'd call it with `!hug mechjack` to hug a user named "mechjack".

|Property Name|Default Value|Notes|
|--|--|--|
|body_required_message_format|$(user), $(trigger) failed: body required|This is the message sent if the custom command didn't already exist and the body was required.|
|description|Lists all the commands available to users.|You can change this to whatever you want, but generally it should be helpful to viewers.|
|message_format|Commands: %s|%s will be substituted with the response body.|
|trigger|!setcommand|This is what a message must start with to trigger the command.|

### Shout Out Commands

#### Shout Out Listener

This command is unlike the others in that is passively watches the chat for *active* participants in your chat that you also follow (lurkers can lurk). It will then shout them out if they haven't been shouted out with the configured number of hours (default `1`). You cannot trigger a shout out actively (this is coming in a future release).

Who you follow is checked every `10` minutes.

* Scope: `tv_mechjack_mechjackbot_command_shoutout_shoutoutlistenerchatcommand`.
* Usage: `!blah`

|Property Name|Default Value|Notes|
|--|--|--|
|description|Monitors chat looking for casters who are due for a shout out.|You can change this to whatever you want, but generally it should be helpful to viewers.|
|frequency.hours|1|The number of hours between shout outs of an individual streamer.|
|message_format|Fellow streamer in chat! Everyone, please give a warm welcome to %1$s. It would be great if you checked them out and gave them a follow. https://twitch.tv/%1$s|`%1$s` will be substituted with the name of the viewer being shouted out.|

### A Note About `%s` and `%1$s`

I know this is awkward and probably confusing. I fully intend to put something easier to use and understand in place in a future release.
