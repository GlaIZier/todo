// Todo add error notifications label
var Task = Task || (function () {

    return {
      saveTask: function () {
        var newTodo = $('#new-task-input').val();
        if (newTodo === '')
          return;
        var apiToken = $.cookie(config.apiTokenCookieName);

        var headers = {};
        headers[config.apiTokenHeaderName] = apiToken;

        $.ajax({
          type: 'POST',
          url: config.apiBaseUrl + config.meTasksEndpoint,
          headers: headers,
          data: 'todo=' + newTodo
        })
          .done(function (response, status, jq) {
            var newTask = {};
            newTask = response.data;
            console.log(newTask);
          })
          .fail(function (xhr, status, error) {
            console.error(error);
          });
      }
    }

  }());