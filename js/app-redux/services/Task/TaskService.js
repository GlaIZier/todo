import $ from 'jquery';
import Q from 'q';

// import Promise from 'promise';


class TaskService {
  // We can avoid constructor with config and import here config, but if we have several different configs than it won't
  // work
  constructor(config) {
    this.config = config;
  }

  loadTasks = () => {
    console.debug('Inside TaskService.loadTasks()');
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

}

export default TaskService;