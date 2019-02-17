import React from 'react';
import css from './Configurator.css';

class Configurator extends React.Component {

  render() {
    return (
      <div id="configurator">
        <h1>Configuration for MechJackBot Required</h1>

        <p>
          Thank you for using MechJackBot. Before you can start your chat bot we
          need to configure MechJackBot with credentials so that it can log into
          chat, take actions on your behalf and use the Twitch API to gather
          information. There are three sets of credentials needed and this page
          will walk you through how to acquire each set.
        </p>

        <form>

          <h2>Broadcaster Twitch Credentials</h2>

          <p>
            The first set of credentials that MechJackBot needs are your own
            Twitch Credentials. In the first field just type in your Twitch
            login. For example I would enter <code>mechjack</code>. The next
            thing you're going to need is an OAuth token that will act as your
            password. How OAuth works isn't important just know that it acts
            like a password used just for this application. You can get a OAuth
            token using
            <a href="https://twitchapps.com/tmi/" target="_blank" rel="noopener noreferrer">
            Twitch Chat OAuth Password Generator</a> and follow the directions
            to get an oauth token to use as your password. Make sure to keep the
            <code>oauth:</code> at the start of the password.
          </p>

          <p>
            <label for="broadcaster-twitch-login">Broadcaster Twitch Login:</label>
            <input type="text" name="broadcasterTwitchLogin"
                id="broadcaster-twitch-login"/>
          </p>

          <p>
            <label for="broadcaster-twitch-password">Broadcaster Twitch Password:</label>
            <input type="password" name="broadcasterTwitchPassword"
                id="broadcaster-twitch-password"/>
          </p>

          <h2>Chat Bot Twitch Credentials</h2>

          <p>
            <label for="chat-bot-twitch-login">Chat Bot Twitch Login:</label>
            <input type="text" name="chatBotTwitchLogin"
                id="chat-bot-twitch-login"/>
          </p>

          <p>
            <label for="chat-bot-twitch-password">Chat Bot Twitch Password:</label>
            <input type="password" name="chatBotTwitchPassword"
                id="chat-bot-twitch-password"/>
          </p>

          <h2>Twitch Application Credentials</h2>

          <p>
            <label for="twitch-client-id">Twitch Client ID:</label>
            <input type="text" name="twitchClientId"
                id="twitch-client-id"/>
          </p>

          <p>
            <label for="twitch-client-secret">Twitch Client Secret:</label>
            <input type="password" name="twitchClientSecret"
                id="twitch-client-secret"/>
          </p>

          </form>
       </div>
     )

  }

}

export default Configurator;
