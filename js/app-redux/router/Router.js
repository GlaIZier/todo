import React from 'react';
import {browserHistory, IndexRedirect, Route, Router} from 'react-router';
import {App} from '../containers/App';
import {LoginPage} from '../pages/LoginPage';
import {RegisterPage} from '../pages/RegisterPage';
import {TasksPage} from '../pages/TasksPage';
import {TaskPage} from '../pages/TaskPage';
import {NotFound} from '../pages/NotFound';
import {requireAuthentication} from '../pages/AuthenticatedPage';
import NoPermissions from '../pages/NoPermissions/NoPermissions';
//import { useBasename } from 'history';

export default function RouterContainer() {
  // <Router history={useBasename(() => browserHistory)({ basename: '/content-classifier' })}
  // If different router for Local is needed then we can check: if (__LOCAL__) {...} else {...}

  return (
    <Router history={browserHistory}>
      <Route path="/todo/spa" component={App}>
        <IndexRedirect to="tasks"/>
        <Route path="login" component={LoginPage} key="login"/>
        <Route path="register" component={RegisterPage} key="register"/>
        <Route path="tasks" component={requireAuthentication(TasksPage)} key="tasks"/>
        <Route path="tasks/:taskId" component={requireAuthentication(TaskPage)} key="task"/>
        <Route path="403" component={NoPermissions} key="noPermissions"/>
        <Route path="*" component={NotFound}/>
      </Route>
    </Router>);
}
