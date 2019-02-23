import React from 'react';
import mergeClassNames from './_common_merge_class_names';

export default class Button extends React.Component {

  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }

  handleClick(event) {
    event.preventDefault();
    if (this.props.onClick) {
      this.props.onClick(event);
    }
  }

  render() {
    const classNames = mergeClassNames(this.props, ['btn']);

    if (this.props.block) {
      classNames.push('btn-block');
    }

    if (this.props.status) {
      classNames.push('btn-' + this.props.status);
    } else if (this.props.category) {
      classNames.push('btn-' + this.props.category);
    }

    return (
      <button type={this.props.type || 'button'}
        className={classNames.join(' ')} onClick={this.handleClick}
        disabled={this.props.disabled}>
        {this.props.children}
      </button>
    );
  }
}
