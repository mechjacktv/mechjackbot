const handleJsonResponse = (input, response, handler) => {
  response.json().then(json => {
    console.debug(JSON.stringify({
      resource: input,
      status: response.status,
      response: JSON.stringify(json)
    }));
    handler(json, response);
  });
}

const handleTextResponse = (input, response, handler) => {
  handler({
    resource: input,
    status: '500',
    message: 'Unsupported Media Type'
  }, response);
  // TODO figure out why response#text is hanging
  response.text(text => {
    console.error(JSON.stringify({
      resource: input,
      status: response.status,
      response: text
    }));
  });
}

module.exports = ({
  input = '/',
  init = { /* empty */ },
  success = () => { /* empty */ },
  failure = () => { /* empty */ },
} = { /* empty */ }) => {
  fetch(input, init).then(response => {
    const contentType = response.headers.get('Content-Type');

    if (contentType.startsWith('application/json')) {
      if (response.ok) {
        handleJsonResponse(input, response, success);
      } else {
        handleJsonResponse(input, response, failure);
      }
    } else {
      handleTextResponse(input, response, failure);
    }
  });
}
