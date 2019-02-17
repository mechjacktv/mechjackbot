import React from 'react';
import css from './App.css';
import Configurator from './Configurator';
import Dashboard from './Dashboard';
import SplashScreen from './SplashScreen';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loadComponent: 'SplashScreen'
    }
    this.handleChatBotReadyResponse.bind(this);
  }

  componentDidMount() {
    fetch('/api/v1/chat-bot/ready')
        .then(function(response) {
          return response.json();
        })
        .then((json) => this.handleChatBotReadyResponse(json));
  }

  handleChatBotReadyResponse(chatBotReadyJson) {
//    if(chatBotReadyJson.ready) {
//      this.setState({
//        loadComponent: 'Dashboard'
//      })
//    } else {
//      this.setState({
//        loadComponent: 'Configurator'
//      })
//    }
    this.setState({
      loadComponent: 'Configurator'
    });
  }

  render() {
    if(this.state.loadComponent === 'SplashScreen') {
      return <SplashScreen/>
    } else if(this.state.loadComponent === 'Configurator') {
      return <Configurator/>
    } else if(this.state.loadComponent === 'Dashboard') {
      return <Dashboard/>
    } else {
      return <h1>Error Starting MechJackBot!</h1>
    }
  }

}

export default App;

