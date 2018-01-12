import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {
  addTaskSagaAC,
  deleteTaskSagaAC,
  getLoading,
  getTasks,
  loadTasksSagaAC,
  updateTaskSagaAC
} from '../../redux/task';
import ReactDOM from 'react-dom';
import './styles/tasks.css';
import {List} from 'immutable';

class Tasks extends PureComponent {

  static propTypes = {
    tasks: PropTypes.array.isRequired,
    loading: PropTypes.bool.isRequired,
    loadTasksSagaAC: PropTypes.func.isRequired,
    addTaskSagaAC: PropTypes.func.isRequired,
    updateTaskSagaAC: PropTypes.func.isRequired
  };

  constructor(props) {
    super(props);
    this.state = {
      updatedTasks: List([]),
      newTodo: ''
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
    let newTaskInput = ReactDOM.findDOMNode(this.newTaskInput);
    const newTodo = newTaskInput.value;
    newTaskInput.value = '';
    if (newTodo === '')
      return;

    this.props.addTaskSagaAC(newTodo);
  };

  handleClickUpdateTask = (e, prevTask) => {
    e.preventDefault();
    e.stopPropagation();

    this.setState({updatedTasks: this.state.updatedTasks.update(prevTask.id, () => prevTask.todo)});
  };

  handleUpdateTask = (e, prevTask) => {
    // esc key
    if (e.keyCode === 27)
      this.setState({updatedTasks: this.state.updatedTasks.update(prevTask.id, () => undefined)});
    // enter key
    if (e.keyCode !== 13)
      return;

    const updatedTodo = e.target.value;
    this.setState({updatedTasks: this.state.updatedTasks.update(prevTask.id, () => undefined)});
    this.props.updateTaskSagaAC(prevTask.id, updatedTodo);
  };

  handleDeleteTask = (e, taskId) => {
    e.preventDefault();
    e.stopPropagation();

    this.props.deleteTaskSagaAC(taskId);
  };

  render() {
    const self = this;
    const tasksContainer = (
      <div className="todos" id="todos">
        {this.props.tasks.map(function (task, i) {
          return (self.state.updatedTasks.get(task.id) !== undefined) ?
            <input
              key={i}
              id="update-task-input"
              type="text"
              className="todo-input form-control"
              disabled={self.props.loading}
              defaultValue={task.todo}
              onKeyUp={(e) => self.handleUpdateTask(e, task)}
            />
            :
            <div className="todo well well-sm" key={i}>
              <div>
                <span className="todo-text" disabled={self.props.loading}
                      onClick={(e) => self.handleClickUpdateTask(e, task)}
                      id={task.id}>{task.todo}</span>
                <span className="todo-remove todo-clickable glyphicon glyphicon-remove" aria-hidden="true"
                      disabled={self.props.loading} onClick={(e) => self.handleDeleteTask(e, task.id)}/>
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
                     disabled={this.props.loading}
                     defaultValue=''
                     ref={(input) => {
                       this.newTaskInput = input
                     }}
                     onKeyUp={this.handlePressSaveTask}
              />
              <span className="todo-new todo-clickable input-group-addon" id="basic-addon"
                    disabled={this.props.loading} onClick={this.handleAddTask}>New task</span>
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

export default connect(mapStateToProps, {loadTasksSagaAC, addTaskSagaAC, updateTaskSagaAC, deleteTaskSagaAC})(Tasks)