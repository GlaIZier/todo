import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {Button, Col, ControlLabel, Form, FormControl, FormGroup, Row} from 'react-bootstrap';
import {getErrorMessage, getHasError, getProcessing, loginSagaAC} from '../../redux/auth';

class Login extends PureComponent {

  static propTypes = {
    loginSagaAC: PropTypes.func.isRequired,
    processing: PropTypes.bool.isRequired,
    hasError: PropTypes.bool.isRequired,
    errorMessage: PropTypes.string.isRequired
  };

  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
    };
  }

  handleUserChange = (event) => {
    this.setState({username: event.target.value});
  };

  handlePasswordChange = (event) => {
    this.setState({password: event.target.value});
  };

  handleLogin = (e) => {
    e.preventDefault();
    e.stopPropagation();

    this.props.loginSagaAC({...this.state});
  };

  render() {

    const styles = {
      scrollableContent: {
        paddingTop: '130px',
        paddingBottom: '170px'
      }
    };

    const invalidCredentialsError = (this.props.hasError) &&
      <Row className="row">
        <Col md={3}/>
        <Col md={6}>
          <p className="alert alert-danger">{this.props.errorMessage}</p>
        </Col>
        <Col md={3}/>
      </Row>;

    return (
      <div id="login-component">
        <div className="scrollable-content" style={styles.scrollableContent}>
          <div className="container">

            {/*<div ng-if="servererror"  className="row">*/}
            {/*<div className="col-md-3"></div>*/}
            {/*<div className="col-md-6">*/}
            {/*<p className="alert alert-danger">Cannot log you in. Server responses '{{errormessage}}'.</p>*/}
            {/*</div>*/}
            {/*<div className="col-md-3"></div>*/}
            {/*</div>*/}

            {invalidCredentialsError}

            <Row>
              <Col md={3}/>
              <Col md={6}>
                <Form
                  horizontal
                  onSubmit={this.handleLogin}>
                  <fieldset>
                    <FormGroup>
                      <ControlLabel htmlFor="username" className="col-sm-3">User Name</ControlLabel>
                      <Col md={9}>
                        <FormControl
                          id="username"
                          type="text"
                          value={this.state.username}
                          onChange={this.handleUserChange}
                          placeholder="Enter your username"/>
                      </Col>
                    </FormGroup>
                    <FormGroup>
                      <ControlLabel htmlFor="password" className="col-sm-3">Password</ControlLabel>
                      <Col md={9}>
                        <FormControl
                          id="password"
                          type="password"
                          value={this.state.password}
                          onChange={this.handlePasswordChange}
                          placeholder="Enter your password"/>
                      </Col>
                    </FormGroup>
                    <FormGroup>
                      <Col sm={9} smOffset={3}>
                        <Button
                          className="btn-primary"
                          type="submit"
                          disabled={this.props.processing}>
                          Log In
                        </Button>
                      </Col>
                    </FormGroup>
                  </fieldset>
                </Form>
              </Col>
              <Col md={3}/>
            </Row>
          </div>
        </div>
      </div>
    )
  }
}

function mapStateToProps(state) {
  return {
    hasError: getHasError(state),
    errorMessage: getErrorMessage(state),
    processing: getProcessing(state)
  }
}

// mapDispatchToProps as second arg. Each function inside is assumed to be an action creator
export default connect(mapStateToProps, {loginSagaAC})(Login)
