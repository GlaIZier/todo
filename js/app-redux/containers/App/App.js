import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import Header from '../../components/Header/index';
import Notifications from '../../components/Notifications';
import Footer from '../../components/Footer/index';
import {connect} from 'react-redux';
import {getUser, logoutSagaAC} from '../../redux/auth';
import {getNotifications} from '../../redux/notifications';


class App extends PureComponent {

  static propTypes = {
    user: PropTypes.object,
    notifications: PropTypes.array,
    logoutSagaAC: PropTypes.func.isRequired,
  };

  render() {
    return (
      <div className='app-container'>
        <Header logout={this.props.logoutSagaAC} user={this.props.user}/>
        <Notifications notifications={this.props.notifications}/>
        {this.props.children}
        <Footer/>
      </div>
    )
  }
}

function mapStateToProps(state) {
  return {
    user: getUser(state),
    notifications: getNotifications(state)
  }
}

export default connect(mapStateToProps, {logoutSagaAC})(App)