import React from 'react';
import {browserHistory, IndexRedirect, Route, Router} from 'react-router';
//import { useBasename } from 'history';
import {App} from '../containers/App';
import {LoginPage} from '../pages/LoginPage';
import {ArticlesPage} from '../pages/ArticlesPage';
import {ArticlePage} from '../pages/ArticlePage';
import {NotFound} from '../pages/NotFound';
import {requireAuthentication} from '../pages/AuthenticatedPage';
import NoPermissions from '../pages/NoPermissions/NoPermissions';

export default function RouterContainer() {
    //<Router history={useBasename(() => browserHistory)({ basename: '/content-classifier' })}

    if (__LOCAL__) {
        return (
            <Router history={browserHistory}>
                <Route path="/" component={App}>
                    <IndexRedirect to="articles"/>
                    <Route path="login" component={LoginPage} key="login"/>
                    <Route path="articles" component={requireAuthentication(ArticlesPage)} key="articles"/>
                    <Route path="articles/:articleId" component={requireAuthentication(ArticlePage)} key="article"/>
                    <Route path="403" component={NoPermissions} key="noPermissions"/>
                    <Route path="*" component={NotFound}/>
                </Route>
            </Router>);
    } else {
        return (
            <Router history={browserHistory}>
                <Route path="/content-classifier/" component={App}>
                    <IndexRedirect to="articles"/>
                    <Route path="login" component={LoginPage} key="login"/>
                    <Route path="articles" component={requireAuthentication(ArticlesPage)} key="articles"/>
                    <Route path="articles/:articleId" component={requireAuthentication(ArticlePage)} key="article"/>
                    <Route path="403" component={NoPermissions} key="noPermissions"/>
                    <Route path="*" component={NotFound}/>
                </Route>
            </Router>);
    }

}
