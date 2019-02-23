import React from 'react';
import mergeClassNames from './_common_merge_class_names';
import StatusEnum from './status_enum'

class ValidationList extends React.Component {
  render() {
    const classNames = mergeClassNames(this.props, ['list-group']);

    return (
      <ul className={classNames.join(' ')}>
        {this.props.children}
      </ul >
    );
  }
}
export default ValidationList;

class ValidationListItem extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      // state
      active: false,
      message: props.children,
      status: StatusEnum.undefined,

      // behavior
      validator: props.validator,
    }
  }

  componentDidMount() {
    if (this.state.validator) {
      setTimeout(() => {
        this.state.validator((status, message) => {
          this.setState({
            active: false,
            message: message,
            status: status,
          });
        });
      }, 1);
      this.setState({
        active: true,
      });
    } else {
      this.setState({
        active: false,
        status: StatusEnum.success,
      });
    }
  }

  render() {
    const classNames = mergeClassNames(this.props, [
      'list-group-item',
      'd-flex',
      'align-items-center',
    ]);
    const progress = this.state.active ? (
      <div className="spinner-border spinner-border-sm ml-auto"
        role="status"
        aria-hidden="true">
        <span className="sr-only">Loading...</span>
      </div>
    ) : '';

    if (this.state.status) {
      classNames.push('list-group-item-' + this.state.status);
    }
    return (
      <li className={classNames.join(' ')}>
        <div className="message">{this.state.message}</div>
        {progress}
      </li>
    );
  }
}
ValidationList.Item = ValidationListItem;