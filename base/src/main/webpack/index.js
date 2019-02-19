import './index.html';
import './index.scss';

import React from 'react';
import ReactDOM from 'react-dom';
import Button from 'react-bootstrap/Button';
import ListGroup from 'react-bootstrap/ListGroup';

class App extends React.Component {
  state = {
    applicaiton: {
      running: false,
      variant: "warning",
    },
    broadcaster: {
      running: false,
      variant: "warning",
    },
    chatBot: {
      running: false,
      variant: "warning",
    },
  };

  componentDidMount() {
    // empty

  }

  render() {
    const variant = this.state.variant;
    const applicaitonVariant = this.state.applicaiton.variant;
    const broadcasterVariant = this.state.broadcaster.variant;
    const chatBotVariant = this.state.chatBot.variant;

    return (
      <div className="App">
        <ListGroup>
          <ListGroup.Item variant={applicaitonVariant}>
            Checking application configuration.
          </ListGroup.Item>
          <ListGroup.Item variant={broadcasterVariant}>
            Checking broadcaster configuration.
          </ListGroup.Item>
          <ListGroup.Item variant={chatBotVariant}>
            Checking chat bot configuration.
          </ListGroup.Item>
        </ListGroup>
        <Button className="mt-3" variant={variant} block>
          Checking essential configuration. Please wait.
        </Button>
      </div>
    );
  }
}

ReactDOM.render(<App />, document.getElementById('root'));