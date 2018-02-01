var Task = Task || (function () {

    var _host = window.location.protocol + '//' + window.location.host;

    var
      _finishUpdateTask = function (self, updatedTodo) {
      var updatedTask = $('<span class="todo-text" onclick="Task.clickUpdateTask(this)">' + updatedTodo + '</span>');
      var parent = $(self).parent();
      $(self).remove();
      parent.prepend(updatedTask);
      parent.find('.todo-remove').show();
    };

    var _getApiTokenHeader = function () {
      var apiToken = $.cookie(config.apiTokenCookieName);

      var headers = {};
      headers[config.apiTokenHeaderName] = apiToken;
      return headers;
    };

    var _processUnauthenticated = function (status) {
      if (status === 401 || status === 403) {
        window.location.replace("./logout");
        return true;
      }
      return false;
    };

    return {

      pressSaveTask: function (event) {
        if (event.keyCode === 13)
          Task.saveTask();
      },

      saveTask: function () {
        var newTaskInput = $('#new-task-input');
        var newTodo = newTaskInput.val();
        newTaskInput.val('');
        if (newTodo === '')
          return;

        var headers = _getApiTokenHeader();
        $.ajax({
          type: 'POST',
          url: _host + config.apiBaseUrl + config.meTasksEndpoint,
          headers: headers,
          data: 'todo=' + newTodo
        })
          .done(function (response, status, jq) {
            var newTask = response.data;
            var newTaskElement =
              '<div id="' + newTask.id + '" class="todo well well-sm">' +
              '<span class="todo-text" onclick="Task.clickUpdateTask(this)">' + newTask.todo + '</span>' +
              '<span class="todo-remove clickable glyphicon glyphicon-remove" aria-hidden="true" onclick="Task.deleteTask(this)"></span>' +
              '</div>';
            $(newTaskElement).hide().appendTo('#todos').show('slow');
            // $('#todos').append(newTaskElement);
            console.log("Created task: " + JSON.stringify(newTask));
          })
          .fail(function (xhr, status, error) {
            if (_processUnauthenticated(xhr.status)) {
              alert("You have been logged out");
              return;
            }
            console.error(error);
            alert("Error: " + error + ". Try to reload the page");
          });
      },

      clickUpdateTask: function (self) {
        var parent = $(self).parent();
        var prevTodo = $(self).text();
        var updateInputElement =
          $('<input id="update-task-input" type="text" class="todo-input form-control" value="' + prevTodo + '" ' +
            'aria-describedby="basic-addon" onkeyup="Task.updateTask(event, this, \'' + prevTodo + '\')">');

        $(self).remove();
        parent.find('.todo-remove').hide();
        parent.prepend(updateInputElement);
        updateInputElement.select();
      },

      updateTask: function (event, self, prevTodo) {
        if (event.keyCode === 27)
          _finishUpdateTask(self, prevTodo);
        if (event.keyCode !== 13)
          return;

        var id = $(self).parent().attr('id');
        var headers = _getApiTokenHeader();
        var updatedTodo = $(self).val();
        $.ajax({
          type: 'PUT',
          url: _host + config.apiBaseUrl + config.meTasksEndpoint + "/" + id + "?todo=" + updatedTodo,
          headers: headers
        })
          .done(function (response, status, jq) {
            var updatedTask = response.data;
            _finishUpdateTask(self, updatedTodo);
            console.log("Updated task: " + JSON.stringify(updatedTask));
          })
          .fail(function (xhr, status, error) {
            if (_processUnauthenticated(xhr.status)) {
              alert("You have been logged out");
              return;
            }
            console.error(error);
            alert("Error: " + error + ". Try to reload the page");
            _finishUpdateTask(self, prevTodo)
          });

      },

      deleteTask: function (self) {
        var parentElement = $(self).parent();
        var id = $(parentElement).attr('id');

        var headers = _getApiTokenHeader();
        $.ajax({
          type: 'DELETE',
          url: _host + config.apiBaseUrl + config.meTasksEndpoint + "/" + id,
          headers: headers
        })
          .done(function (response, status, jq) {
            $(parentElement).hide('slow', function () {
              $(parentElement).remove();
            });
            console.log("Deleted task: " + JSON.stringify(response.data));
          })
          .fail(function (xhr, status, error) {
            if (_processUnauthenticated(xhr.status)) {
              alert("You have been logged out");
              return;
            }
            console.error(error);
            alert("Error: " + error + ". Try to reload the page");
          });
      }
    }

  }());