import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {Login} from '../../containers/Login';
import {navigateSagaAC} from '../../redux/navigate';
import {connect} from 'react-redux';
import {getUser} from '../../redux/auth';

// Added this Login wrapper in case of different locales need
// All redirects are in pages
class LoginPage extends PureComponent {

  static propTypes = {
    user: PropTypes.object,
    navigateSagaAC: PropTypes.func.isRequired
  };

  // Go to root if authenticated
  componentWillMount() {
    this.checkAuth(this.props.user)
  }

  componentWillReceiveProps(nextProps) {
    this.checkAuth(nextProps.user)
  }

  checkAuth = (user) => {
    if (user)
      this.props.navigateSagaAC('root')
  };

  render() {
    return (
      <Login/>
    );
  }
}

function mapStateToProps(state) {
  return {
    user: getUser(state)
  }
}

export default connect(mapStateToProps, {navigateSagaAC})(LoginPage)
