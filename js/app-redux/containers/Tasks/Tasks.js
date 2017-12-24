import React, {PureComponent} from 'react';
// import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {searchSagaAC} from '../../redux/search';
import {loadTasksSagaAC} from '../../redux/tasks';
import {articleNavigateSagaAC} from '../../redux/navigate';
import './styles/tasks.css';

class Tasks extends PureComponent {

  // static propTypes = {
  //     filters: PropTypes.object.isRequired,
  //     processing: PropTypes.bool.isRequired,
  //     searchSagaAC: PropTypes.func.isRequired,
  //     articles: PropTypes.array.isRequired,
  //     nextPageOffset: PropTypes.number,
  //     loadTasksSagaAC: PropTypes.func.isRequired,
  //     loading: PropTypes.bool.isRequired,
  //     articleNavigateSagaAC: PropTypes.func.isRequired
  // };

  render() {
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
          <div className="todos" id="todos">
            <div className="todo well well-sm">
              <span className="todo-text" onclick="Task.clickUpdateTask(this)">Just do something else</span>
              <span className="todo-remove clickable glyphicon glyphicon-remove" aria-hidden="true"
                    onclick="Task.deleteTask(this)"></span>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

function mapStateToProps(state) {
  console.log(state);
  return {
    // filters: getFilters(state),
    // processing: getProcessing(state),
    // articles: getTasks(state),
    // nextPageOffset: getErrorMessage(state),
    // loading: getLoading(state)
  }
}

export default connect(mapStateToProps, {searchSagaAC, loadTasksSagaAC, articleNavigateSagaAC})(Tasks)