import React from 'react';
import {browserHistory, IndexRedirect, Route, Router} from 'react-router';
//import { useBasename } from 'history';
import {App} from '../containers/App';
import {LoginPage} from '../pages/LoginPage';
import {TasksPage} from '../pages/TasksPage';
import {ArticlePage} from '../pages/ArticlePage';
import {NotFound} from '../pages/NotFound';
import {requireAuthentication} from '../pages/AuthenticatedPage';
import NoPermissions from '../pages/NoPermissions/NoPermissions';

export default function RouterContainer() {
  //<Router history={useBasename(() => browserHistory)({ basename: '/content-classifier' })}

  // Todo try to import here constants from routes
  if (__LOCAL__) {
    console.log('Creating router for local profile...');
    return (
      <Router history={browserHistory}>
        <Route path="/" component={App}>
          <IndexRedirect to="tasks"/>
          <Route path="login" component={LoginPage} key="login"/>
          <Route path="tasks" component={requireAuthentication(TasksPage)} key="tasks"/>
          <Route path="tasks/:taskId" component={requireAuthentication(ArticlePage)} key="task"/>
          <Route path="403" component={NoPermissions} key="noPermissions"/>
          <Route path="*" component={NotFound}/>
        </Route>
      </Router>);
  } else {
    console.log('Creating router for not local profile...');
    return (
      <Router history={browserHistory}>
        <Route path="/todo/spa/" component={App}>
          <IndexRedirect to="tasks"/>
          <Route path="login" component={LoginPage} key="login"/>
          <Route path="tasks" component={requireAuthentication(TasksPage)} key="tasks"/>
          <Route path="tasks/:taskId" component={requireAuthentication(ArticlePage)} key="task"/>
          <Route path="403" component={NoPermissions} key="noPermissions"/>
          <Route path="*" component={NotFound}/>
        </Route>
      </Router>);
  }

}
