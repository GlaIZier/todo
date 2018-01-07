import $ from 'jquery';
import Q from 'q';
import Cookies from 'js-cookie';

// import Promise from 'promise';


class TaskService {
  // We can avoid constructor with config and import here config, but if we have several different configs than it won't
  // work
  constructor(config) {
    this.config = config;
  }

  loadTasks = () => {
    const defer = Q.defer();

    $.ajax({
      url: `${this.config.tasksApiUrl}`,
      method: 'GET',
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      }
    })
      .then(payload => defer.resolve(payload))
      .fail(e => defer.reject(e));

    return defer.promise;
  };

  addTask = (todo = '') => {
    const defer = Q.defer();

    const token = Cookies.get(this.config.constants.apiTokenCookieName);
    const headers = {};
    headers[this.config.constants.apiTokenHeaderName] = token;

    const data = {};
    data['todo'] = todo;

    $.ajax({
      url: `${this.config.tasksApiUrl}`,
      method: 'POST',
      headers: headers,
      crossDomain: true,
      data: data,
      xhrFields: {
        withCredentials: true
      }
    })
      .then(payload => defer.resolve(payload))
      .fail(e => defer.reject(e));

    return defer.promise;
  };

  updateTask = (id = null, todo = '') => {
    const defer = Q.defer();

    const token = Cookies.get(this.config.constants.apiTokenCookieName);
    const headers = {};
    headers[this.config.constants.apiTokenHeaderName] = token;

    $.ajax({
      url: `${this.config.tasksApiUrl}/${id}?todo=${todo}`,
      method: 'PUT',
      headers: headers,
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      }
    })
      .then(payload => defer.resolve(payload))
      .fail(e => defer.reject(e));

    return defer.promise;
  };

  deleteTask = (id = null) => {
    const defer = Q.defer();

    const token = Cookies.get(this.config.constants.apiTokenCookieName);
    const headers = {};
    headers[this.config.constants.apiTokenHeaderName] = token;

    $.ajax({
      url: `${this.config.tasksApiUrl}/${id}`,
      method: 'DELETE',
      headers: headers,
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      }
    })
      .then(payload => defer.resolve(payload))
      .fail(e => defer.reject(e));

    return defer.promise;
  };


}

export default TaskService;