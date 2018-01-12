import React from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {getUser, logoutSagaAC} from '../../redux/auth';
import {navigateSagaAC} from '../../redux/navigate';
import Cookies from 'js-cookie';
import config from '../../config/config.common';

/**
 * Different approach to create authentication in React is to check codes of errors during services calls and navigate
 * to login page in case of 401 or 403
 */
export default function requireAuthentication(PureComponent) {

  class AuthenticatedPage extends React.PureComponent {

    static propTypes = {
      user: PropTypes.object,
      navigateSagaAC: PropTypes.func.isRequired,
      logoutSagaAC: PropTypes.func.isRequired
    };

    // Go to login if unauthenticated
    componentWillMount() {
      this.checkAuth(this.props.user)
    }

    componentWillReceiveProps(nextProps) {
      this.checkAuth(nextProps.user)
    }

    /**
     * If there is no cookie and user => cookie is expired => need to logout
     * If no user is presented => need to login
     */
    checkAuth(user) {
      const apiTokenCookie = Cookies.get(config.constants.apiTokenCookieName);
      if (apiTokenCookie === undefined && user) {
        this.props.logoutSagaAC();
        return;
      }

      if (!user)
        this.props.navigateSagaAC('login')
    }

    render() {
      return (
        <div>
          {(this.props.user)
            ? <PureComponent {...this.props} />
            : <h3>401 Unauthorized!</h3>
          }
        </div>
      )
    }
  }

  function mapStateToProps(state) {
    return {
      user: getUser(state)
    }
  }

  return connect(mapStateToProps, {navigateSagaAC, logoutSagaAC})(AuthenticatedPage)
}