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
              '<span class="todo-remove clickable glyphicon glyphicon-remove" aria-hidden="true" onclick="Task.deleteTask(this)"></span>' +
              '</div>';
            $(newTaskElement).hide().appendTo('#todos').show('slow');
            // $('#todos').append(newTaskElement);
            console.log("Created task: " + JSON.stringify(newTask));
          })
          .fail(function (xhr, status, error) {
            console.error(error);
            alert("Error: " + error + ". Try to reload the page");
          });
      },

      clickUpdateTask: function (clickedElement) {
        var parent = $(clickedElement).parent();
        var id = parent.attr('id');
        var todo = $(clickedElement).text();
        var updateInputElement =
          $('<input id="new-task-input" type="text" class="todo-input form-control" value="' + todo + '" aria-describedby="basic-addon">');

        $(clickedElement).remove();
        parent.find('.todo-remove').hide();
        parent.prepend(updateInputElement);
        updateInputElement.select();
      },

      updateTask: function (clickedElement) {

      },

      deleteTask: function (clickedElement) {
        var parentElement = $(clickedElement).parent();
        var id = $(parentElement).attr('id');

        var apiToken = $.cookie(config.apiTokenCookieName);
        var headers = {};
        headers[config.apiTokenHeaderName] = apiToken;

        $.ajax({
          type: 'DELETE',
          url: config.apiBaseUrl + config.meTasksEndpoint + "/" + id,
          headers: headers
        })
          .done(function (response, status, jq) {
            $(parentElement).hide('slow', function () {
              $(parentElement).remove();
            });
            console.log("Deleted task: " + JSON.stringify(response.data));
          })
          .fail(function (xhr, status, error) {
            console.error(error);
            alert("Error: " + error + ". Try to reload the page");
          });
      }
    }

  }());