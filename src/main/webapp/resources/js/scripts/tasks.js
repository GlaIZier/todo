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
            var newTask = response.data;
            var newTaskElement =
              '<div id="' + newTask.id + '" class="todo well well-sm">' +
              '<span class="todo-text">' + newTask.todo + '</span>' +
              '<span class="todo-remove clickable glyphicon glyphicon-remove" aria-hidden="true"></span>' +
              '</div>';
            $('#todos').append(newTaskElement);
            console.log("Created task: " + JSON.stringify(newTask));
          })
          .fail(function (xhr, status, error) {
            console.error(error);
            alert("Error: " + error + ". Try to reload the page");
          });
      }
    }

  }());