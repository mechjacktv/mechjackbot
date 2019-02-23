import React from 'react';
import Button from './button';
import mergeClassNames from './_common_merge_class_names';
import uuid from 'uuid/v4';

export default class Form extends React.Component {
  render() {
    const classNames = mergeClassNames(this.props);

    return (
      <form className={classNames.join(' ')}>
        {this.props.children}
      </form>
    );
  }
}

class FormGroup extends React.Component {
  render() {
    const classNames = mergeClassNames(this.props, ['form-group']);

    return (
      <div className={classNames.join(' ')}>
        {this.props.children}
      </div>
    );
  }
}
Form.Group = FormGroup;

class FormLabel extends React.Component {
  render() {
    const classNames = mergeClassNames(this.props);

    return (
      <label className={classNames.join(' ')} htmlFor={this.props.htmlFor}>
        {this.props.children}
      </label>
    );
  }
}
Form.Label = FormLabel;

class FormTextInput extends React.Component {
  render() {
    const classNames = mergeClassNames(this.props, ['form-control']);

    return (
      <input type="text"
        id={this.props.id}
        className={classNames.join(' ')}
        name={this.props.id}
        autoComplete={this.props.autoComplete}
        placeholder={this.props.placeholder}
        value={this.props.value}
        onChange={this.props.onChange} />
    );
  }
}
Form.TextInput = FormTextInput;

class FormPasswordInput extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      buttonId: uuid(),
      showing: false,
    }
    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      showing: !this.state.showing,
    });
  }

  render() {
    const classNames = mergeClassNames(this.props, ['input-group']);
    const buttonId = this.state.buttonId;
    const showing = this.state.showing;

    return (
      <div className={classNames.join(' ')}>
        <input type={showing ? 'test' : 'password'}
          id={this.props.id}
          className="form-control"
          name={this.props.id}
          autoComplete={this.props.autoComplete}
          placeholder={this.props.placeholder}
          aria-label={this.props.placeholder}
          aria-describedby={buttonId}
          value={this.props.value}
          onChange={this.props.onChange} />
        <div className="input-group-append">
          <button id={buttonId} className="btn btn-secondary" type="button"
            onClick={this.toggle}>
            {showing ? 'Hide' : 'Show'}
          </button>
        </div>
      </div>
    );
  }
}
Form.PasswordInput = FormPasswordInput;

class FormSubmitButton extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      active: false,
    }
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleSubmitted = this.handleSubmitted.bind(this);
  }

  handleSubmit() {
    if (this.props.onClick) {
      this.setState({
        active: true
      });
      this.props.onClick(this.handleSubmitted);
    }
  }

  handleSubmitted() {
    this.setState({
      active: false
    });
  }

  render() {
    const classNames = mergeClassNames(this.props, [
      'btn',
      'd-flex',
      'align-items-center'
    ]);
    const progress = this.state.active ? (
      <div className="spinner-border spinner-border-sm ml-3"
        role="status"
        aria-hidden="true">
        <span className="sr-only">Loading...</span>
      </div>
    ) : '';

    return <Button type="submit" classNames={classNames.join(' ')}
      category="primary" onClick={this.handleSubmit}
      disabled={this.props.disabled} >
      <div className="children">{this.props.children}</div>
      {progress}
    </Button>
  }
}
Form.SubmitButton = FormSubmitButton;
