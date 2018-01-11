import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import LogoPng from '../../images/todo.png';
import {Button, Modal} from 'react-bootstrap';
import './styles/header.css';

class Header extends PureComponent {

  static propTypes = {
    user: PropTypes.object,
    logout: PropTypes.func.isRequired,
    navigate: PropTypes.func.isRequired
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

  handleNavigate = (e, pageName) => {
    e.preventDefault();
    e.stopPropagation();

    this.props.navigate(pageName);
  };

  render() {
    // const styles = {
    //   navbarHeader: {
    //     float: 'right'
    //   },
    //   navbarBrand: {
    //     borderLeft: '2px solid white'
    //   }
    // };

    // let logout =
    //   (this.props.user) ?
    //     <div className="navbar-header" style={styles.navbarHeader}>
    //           <span className="navbar-brand" style={styles.navbarBrand}>
    //             {this.props.user.username}
    //           </span>
    //       <a className="navbar-brand" onClick={this.openModal}>
    //         <span className="glyphicon glyphicon-log-out" alt="Logout"/>
    //       </a>
    //     </div>
    //     :
    //     null;

    let modal =
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
      </Modal>;

    const userHeader =
      (this.props.user) ?
        <ul className="nav navbar-nav navbar-right">
          <li className="navbar-text">Signed in as <span>{this.props.user.login}</span></li>
          <li><a className="todo-clickable" onClick={this.openModal}>Sign out</a></li>
        </ul>
        :
        <ul className="nav navbar-nav navbar-right">
          <li><a className="todo-clickable" onClick={(e) => {
            this.handleNavigate(e, 'login')
          }}>Sign in</a></li>
          <li><a className="todo-clickable" onClick={(e) => {
            this.handleNavigate(e, 'register')
          }}>Sign up</a></li>
        </ul>;

    //     <div className="top-panel">
    //     <div id="navbar-bootstrap-override" className="navbar navbar-default neutral-dark" role="navigation">
    //     <div className="container">
    //
    //     <div className="navbar-header">
    //     <a href="/content-classifier/"><img className="navbar-logo" src={LogoPng}/></a>
    //     <a className="navbar-brand" href="/content-classifier/">Content Classifier Tool</a>
    //   </div>
    //   {logout}
    // </div>
    // </div>
    // {modal}
    // </div>

    return (
      <nav className="navbar navbar-default">
        <div className="container">
          {/*<!-- Brand and toggle get grouped for better mobile display -->*/}
          <div className="navbar-header">
            <button type="button" className="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
              <span className="sr-only">Toggle navigation</span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
            </button>

            <a className="navbar-left" onClick={(e) => {
              this.handleNavigate(e, 'root')
            }}>
              <img className="todo-clickable" alt="Todo" height="45px" src={LogoPng}/>
            </a>
          </div>

          {/*<!-- Collect the nav links, forms, and other content for toggling -->*/}
          <div className="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul className="nav navbar-nav">
              <li><a className="todo-clickable" onClick={(e) => {
                this.handleNavigate(e, 'tasks')
              }}>Tasks</a></li>
            </ul>
            {userHeader}
          </div>
          {/*<!-- /.navbar-collapse -->*/}
        </div>
        {/*<!-- /.container-fluid -->*/}
        <div>
          {modal}
        </div>
      </nav>
    );
  }
}

export default Header;