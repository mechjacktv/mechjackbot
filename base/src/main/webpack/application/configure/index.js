import './index.html';
import './index.scss';

import React from 'react';
import ReactDOM from 'react-dom';

import Form from '../../component/form'
import StatusEnum from '../../component/status_enum'
import fetchJson from '../../utils/utils_fetch_json'

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      active: false,
      message: null,
      status: StatusEnum.undefined,

      clientId: '',
      clientSecret: '',
    }
    this.inputChangeHandler = this.inputChangeHandler.bind(this);
    this.submitHandler = this.submitHandler.bind(this);
  }

  inputChangeHandler(event) {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;

    this.setState({
      [name]: value
    });
  }

  submitHandler(callback) {
    this.setState({
      active: true,
      message: null,
      status: StatusEnum.undefined,
    });
    fetchJson({
      input: '/api/v1/application',
      init: {
        method: 'PUT',
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          clientId: this.state.clientId,
          clientSecret: this.state.clientSecret,
        }),
      },
      success: (json) => {
        this.setState({
          active: false,
          message: 'MechJackBot is registered!',
          status: StatusEnum.success,
        });
        callback();
      },
      failure: (json) => {
        console.error(`${json.status} - ${json.message}: ${json.resource}`);
        this.setState({
          active: false,
          message: json.message,
          status: json.status.startsWith('5') ?
            StatusEnum.error :
            StatusEnum.failure,
        });
        callback();
      },
    });

  }

  render() {
    const disabled = this.state.active;
    const message = (() => {
      if (this.state.status !== StatusEnum.undefined) {
        return (
          <div className={`mt-3 alert alert-${this.state.status}`} role="alert">
            {this.state.message}
          </div>
        );
      }
    })();

    return (
      <div className="App">
        <Form>
          <Form.Group>
            <Form.Label htmlFor="clientId">
              Twitch Client ID
            </Form.Label>
            <Form.TextInput id="clientId"
              autoComplete="username"
              placeholder="Twtich Client Id"
              value={this.state.clientId}
              onChange={this.inputChangeHandler} />
          </Form.Group>
          <Form.Group>
            <Form.Label htmlFor="clientSecret">
              Twitch Client Secret
            </Form.Label>
            <Form.PasswordInput id="clientSecret"
              autoComplete="new-password"
              placeholder="Twtich Client Secret"
              value={this.state.clientSecret}
              onChange={this.inputChangeHandler} />
          </Form.Group>
          <Form.SubmitButton onClick={this.submitHandler} disabled={disabled}>
            {this.state.active ? 'Saving' : 'Save'}
          </Form.SubmitButton>
          {message}
        </Form>
      </div>
    );
  }
}

ReactDOM.render(<App />, document.getElementById('app'));
