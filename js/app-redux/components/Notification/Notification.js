import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {Alert} from 'react-bootstrap';

class Notification extends PureComponent {

  static propTypes = {
    id: PropTypes.string.isRequired,
    message: PropTypes.string.isRequired,
    bgStyle: PropTypes.string
  };

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Alert bsStyle={this.props.bgStyle}>
        {this.props.message}
      </Alert>
    );
  }
}

export default Notification;