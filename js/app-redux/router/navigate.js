import {browserHistory} from 'react-router';
import routesJson from './routes.json';
import {stringify} from 'query-string';
import _ from 'underscore';

const routes = routesJson;

export const getURL = (pageName, props) => {
  const template = _.template(routes[pageName]);
  const encodedProps = Object.assign({}, props);
  for (const prop in encodedProps) {
    if (encodedProps.hasOwnProperty(prop)) {
      if (typeof encodedProps[prop] === 'object') {
        encodedProps[prop] = stringify(encodedProps[prop], true);
      } else {
        encodedProps[prop] = encodeURIComponent(encodedProps[prop]);
      }
    }
  }
  if (__LOCAL__) {
      return template({props: encodedProps});
  } else {
      return '/content-classifier' + template({props: encodedProps});
  }
};

export const navigate = (pageName, props) => browserHistory.push(getURL(pageName, props));

// Add here additional methods for navigation like navigateReplace, navigateBack ...