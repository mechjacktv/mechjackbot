import './index.html';
import './index.scss';

import React from 'react';
import ReactDOM from 'react-dom';

import Button from './component/button'
import StatusEnum from './component/status_enum'
import ValidationList from './component/validation_list'
import fetchJson from './utils/utils_fetch_json'

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      applicationStatus: StatusEnum.checking,
      broadcasterStatus: StatusEnum.checking,
      chatBotStatus: StatusEnum.checking,
    }
    this.applicationValidator = this.applicationValidator.bind(this);
    this.broadcasterValidator = this.broadcasterValidator.bind(this);
    this.chatBotValidator = this.chatBotValidator.bind(this);
  }

  applicationValidator(callback) {
    fetchJson({
      input: '/api/v1/application',
      success: (json) => {
        if (json.data.registered) {
          callback(StatusEnum.success, 'MechJackBot registered!');
          this.setState({
            applicationStatus: StatusEnum.success
          });
        } else {
          callback(StatusEnum.failure, 'MechJackBot needs to be registered');
          this.setState({
            applicationStatus: StatusEnum.failure
          });
        }
      },
      failure: (json) => {
        console.error(`${json.status} - ${json.message}: ${json.resource}`);
        callback(StatusEnum.error, 'Failed to check MechJackBot registration.');
        this.setState({
          applicationStatus: StatusEnum.error
        });
      },
    });
  }

  broadcasterValidator(callback) {
    callback(StatusEnum.failure, 'Broadcaster needs to be authenticated.');
    this.setState({
      broadcasterStatus: StatusEnum.failure
    });
  }

  chatBotValidator(callback) {
    callback(StatusEnum.failure, 'Chat bot needs to be authenticated.');
    this.setState({
      chatBotStatus: StatusEnum.failure
    });
  }

  render() {
    let button;

    if (this.state.applicationStatus === StatusEnum.error
      || this.state.broadcasterStatus === StatusEnum.error
      || this.state.chatBotStatus === StatusEnum.error) {
      button = (
        <Button className="mt-3" block status={StatusEnum.error} disabled>
          Failure While Performing Checks (Cannot Continue)
        </Button>
      );
    } else if (this.state.applicationStatus === StatusEnum.failure) {
      button = (
        <Button className="mt-3" block status={StatusEnum.failure}
          onClick={() => { window.location.assign('/application/configure') }}>
          Register MechJackBot with Twitch
        </Button>
      );
    } else if (this.state.applicationStatus === StatusEnum.success
      && this.state.broadcasterStatus === StatusEnum.failure) {
      button = (
        <Button className="mt-3" block status={StatusEnum.failure}>
          Authorize Broadcaster
        </Button>
      );
    } else if (this.state.applicationStatus === StatusEnum.success
      && this.state.broadcasterStatus === StatusEnum.success
      && this.state.chatBotStatus === StatusEnum.failure) {
      button = (
        <Button className="mt-3" block status={StatusEnum.failure}>
          Authorize Chat Bot
        </Button>
      );
    } else {
      button = (
        <Button className="mt-3" block disabled>
          Validating Configuration (Please Wait)
        </Button>
      );
    }

    return (
      <div className="App">
        <ValidationList className="test">
          <ValidationList.Item validator={this.applicationValidator}>
            Checking MechJackBot registration.
          </ValidationList.Item>
          <ValidationList.Item validator={this.broadcasterValidator}>
            Checking broadcaster authentication.
          </ValidationList.Item>
          <ValidationList.Item validator={this.chatBotValidator}>
            Checking chat bot authentication.'
          </ValidationList.Item>
        </ValidationList>
        {button}
      </div>
    );
  }
}

ReactDOM.render(<App />, document.getElementById('app'));
