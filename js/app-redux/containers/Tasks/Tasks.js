import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {getLoading, getTasks, loadTasksSagaAC} from '../../redux/task';
import './styles/tasks.css';
// import {articleNavigateSagaAC} from '../../redux/navigate';
// import {searchSagaAC} from '../../redux/search';

class Tasks extends PureComponent {

  static propTypes = {
    tasks: PropTypes.array.isRequired,
    loadTasksSagaAC: PropTypes.func.isRequired,
    loading: PropTypes.bool.isRequired
  };

  componentDidMount() {
    this.props.loadTasksSagaAC();
  }

  render() {
    const tasksContainer = (
      <div className="todos" id="todos">
        {this.props.tasks.map(function (task, i) {
          return <div className="todo well well-sm">
            <div key={i}>
              <span className="todo-text" onclick="Task.clickUpdateTask(this)">{task.todo}</span>
              <span className="todo-remove clickable glyphicon glyphicon-remove" aria-hidden="true"
                    onclick="Task.deleteTask(this)"/>
            </div>
          </div>
        })}
      </div>
    );

    return (
      <div className="container">
        <div className="react-app">
          <header className="header">
            <h2>Todo</h2>
            <div className="todo-input-group input-group">
              <input id="new-task-input" type="text" className="todo-input form-control"
                     placeholder="What needs to be done?"
                     aria-describedby="basic-addon"
                     onkeyup="Task.pressSaveTask(event)"
              />
              <span className="todo-new clickable input-group-addon" id="basic-addon"
                    onclick="Task.saveTask()">New task</span>
            </div>
          </header>
          {tasksContainer}
        </div>
      </div>
    )
  }
}

function mapStateToProps(state) {
  return {
    tasks: getTasks(state),
    loading: getLoading(state)
  }
}

export default connect(mapStateToProps, {loadTasksSagaAC})(Tasks)