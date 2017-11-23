import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import Notification from '../Notification';
import './styles/notifications.css';

class Notifications extends PureComponent {

  static propTypes = {
    notifications: PropTypes.array.isRequired
  };

  constructor(props) {
    super(props);
  }

  render() {

    return (
      <div className="container cc-notification">
        {this.props.notifications.map(function (notification, i) {
          return (
            <Notification
              key={i}
              id={notification.id}
              message={notification.message}
              bgStyle={notification.bgStyle}
            />
          )
        })}
      </div>
    );
  }
}

export default Notifications;