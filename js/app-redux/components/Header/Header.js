import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import LogoPng from '../../images/wiley.png';
import {Button, Modal} from 'react-bootstrap';
import './styles/header.css';


class Header extends PureComponent {

  static propTypes = {
    logout: PropTypes.func.isRequired,
    user: PropTypes.object
  };

  constructor(props) {
    super(props);
    this.state = {
      showModal: false
    }
  }

  openModal = () => {
    this.setState({showModal: true})
  };

  closeModal = () => {
    this.setState({showModal: false})
  };

  handleLogout = (e) => {
    e.preventDefault();
    e.stopPropagation();

    this.props.logout();
    this.closeModal()
  };

  render() {
    const styles = {
      navbarHeader: {
        float: 'right'
      },
      navbarBrand: {
        borderLeft: '2px solid white'
      }
    }

    const logout =
      (this.props.user) ?
        <div className="navbar-header" style={styles.navbarHeader}>
              <span className="navbar-brand" style={styles.navbarBrand}>
                {this.props.user.username}
              </span>
          <a className="navbar-brand" onClick={this.openModal}>
            <span className="glyphicon glyphicon-log-out" alt="Logout"/>
          </a>
        </div>
        :
        null

    const modal =
      <Modal show={this.state.showModal} onHide={this.closeModal}>
        <Modal.Header closeButton>
          <Modal.Title>Logging out</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Are you sure you want to log out?</p>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={this.closeModal}>No</Button>
          <Button bsStyle="warning" onClick={this.handleLogout}>Yes</Button>
        </Modal.Footer>
      </Modal>

    return (
      <div className="top-panel">
        <div id="navbar-bootstrap-override" className="navbar navbar-default neutral-dark" role="navigation">
          <div className="container">

            <div className="navbar-header">
              <a href="/content-classifier/"><img className="navbar-logo" src={LogoPng}/></a>
              <a className="navbar-brand" href="/content-classifier/">Content Classifier Tool</a>
            </div>
            {logout}
          </div>
        </div>
        {modal}
      </div>
    );
  }
}

export default Header;