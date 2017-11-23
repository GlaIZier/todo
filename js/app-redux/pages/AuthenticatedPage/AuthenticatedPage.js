import React from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {getUser} from '../../redux/auth';
import {navigateSagaAC} from '../../redux/navigate';


export default function requireAuthentication(PureComponent) {

  class AuthenticatedPage extends React.PureComponent {

    static propTypes = {
      user: PropTypes.object,
      navigateSagaAC: PropTypes.func.isRequired
    };

    // Go to login if unauthenticated
    componentWillMount() {
      this.checkAuth(this.props.user)
    }

    componentWillReceiveProps(nextProps) {
      this.checkAuth(nextProps.user)
    }

    checkAuth(user) {
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

  return connect(mapStateToProps, {navigateSagaAC})(AuthenticatedPage)
}