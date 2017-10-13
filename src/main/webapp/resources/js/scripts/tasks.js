// Todo add error notifications label
var Task = Task || (function () {

    return {
      saveTask: function () {
        var newTodo = $('#new-task-input').val();
        var apiToken = $.cookie(config.apiTokenCookieName);

        var headers = {};
        headers[config.apiTokenHeaderName] = apiToken;

        $.ajax({
          type: 'POST',
          url: config.apiBaseUrl + config.meTasksEndpoint,
          headers: headers,
          data: newTodo
        })
          .done(function (response, status, jq) {
            console.log(response);
          })
          .fail(function (xhr, status, error) {
            console.error(error);
          });
      }
    }

  }());