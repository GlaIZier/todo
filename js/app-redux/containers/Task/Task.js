import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {getTask, loadTaskSagaAC} from '../../redux/task';
import './styles/task.css';

class Task extends PureComponent {

  static propTypes = {
    taskId: PropTypes.string.isRequired,
    task: PropTypes.object.isRequired
  };

  componentDidMount() {
    this.props.loadTaskSagaAC(this.props.taskId);
  }

  render() {
    return (
      <ul id="todo-task">
        <li>{this.props.task.id}</li>
        <li>{this.props.task.login}</li>
        <li>{this.props.task.todo}</li>
      </ul>
    )
  }
}

function mapStateToProps(state) {
  console.log(`mapStateToProps ${state}`);
  return {
    task: getTask(state)
  }
}

export default connect(mapStateToProps, {loadTaskSagaAC})(Task)