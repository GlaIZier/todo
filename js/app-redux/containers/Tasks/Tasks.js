import React, {PureComponent} from "react";
import PropTypes from "prop-types";
import {connect} from "react-redux";
import {addTaskSagaAC, getLoading, getTasks, loadTasksSagaAC} from "../../redux/task";
import $ from "jquery";
import "./styles/tasks.css";
import {List} from "immutable";

class Tasks extends PureComponent {

  static propTypes = {
    tasks: PropTypes.array.isRequired,
    loadTasksSagaAC: PropTypes.func.isRequired,
    addTaskSagaAC: PropTypes.func.isRequired,
    loading: PropTypes.bool.isRequired
  };

  constructor(props) {
    super(props);
    this.state = {
      updateTaskMode: List([])
    };
  }

  componentDidMount() {
    this.props.loadTasksSagaAC();
  }

  handlePressSaveTask = (e) => {
    if (e.keyCode === 13)
      this.handleAddTask(e);
  };

  handleAddTask = (e) => {
    e.preventDefault();
    e.stopPropagation();
    const newTaskInput = $('#new-task-input');
    const newTodo = newTaskInput.val();
    newTaskInput.val('');
    if (newTodo === '')
      return;

    this.props.addTaskSagaAC(newTodo);
  };

  handleClickUpdateTask = (e, task) => {
    console.log(e);
    console.log(task);
    e.preventDefault();
    e.stopPropagation();

    this.setState({updateTaskMode: this.state.updateTaskMode.update(task.id, () => true)});
  };

  handleUpdateTask = (e, prevTask) => {
    console.log(`handleUpdateTask ${e} ${prevTask}`);
  };

  // Todo check how to create component without
  render() {
    const self = this;
    const tasksContainer = (
      <div className="todos" id="todos">
        {this.props.tasks.map(function (task, i) {
          return (self.state.updateTaskMode.get(task.id) === true) ?
            <input
              key={i}
              id="update-task-input"
              type="text"
              className="todo-input form-control"
              defaultValue={task.todo}
              onKeyUp={(e) => self.handleUpdateTask(e, task.todo)}
            />
            :
            <div className="todo well well-sm" key={i}>
              <div>
                <span className="todo-text" onClick={(e) => self.handleClickUpdateTask(e, task)}
                      id={task.id}>{task.todo}</span>
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
                     onKeyUp={this.handlePressSaveTask}
              />
              <span className="todo-new clickable input-group-addon" id="basic-addon"
                    onClick={this.handleAddTask}>New task</span>
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

export default connect(mapStateToProps, {loadTasksSagaAC, addTaskSagaAC})(Tasks)