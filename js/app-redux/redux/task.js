import {fromJS} from 'immutable';

const initialState = fromJS({
  tasks: [
    // {
    //   "id": 1,
    //   "login": "u",
    //   "todo": "todo1"
    // }
  ],
  loading: false,
  errorMessage: null
});

// reducer
export default function reducer(tasks = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case TASKS_UPDATING_START:
      return tasks.set('loading', true).set('errorMessage', null);

    case TASKS_LOADING_SUCCESS:
      // tasks.get('tasks').concat(fromJS(payload.tasks.tasks))
      return tasks.set('tasks', fromJS(payload.tasks.tasks))
        .set('loading', false).set('errorMessage', null);

    case TASK_ADDING_SUCCESS:
      return tasks.set('tasks', tasks.get('tasks').push(fromJS(payload.task)))
        .set('loading', false).set('errorMessage', null);

    // Todo change logic of updating here here
    case TASK_UPDATING_SUCCESS:
      return tasks.set('tasks', tasks.get('tasks').push(fromJS(payload.task)))
        .set('loading', false).set('errorMessage', null);

    case TASKS_UPDATING_FAIL:
      return tasks.set('loading', false).set('errorMessage', payload.errorMessage);

    default:
      return tasks;
  }
}

// Getters
export const getTasks = state => state.tasks.get('tasks').toJS();
export const getErrorMessage = state => state.tasks.get('errorMessage');
export const getLoading = state => state.tasks.get('loading');

// constants
export const TASKS_LOADING = 'TASKS_LOADING';
export const TASK_ADDING = 'TASK_ADDING';
export const TASK_UPDATING = 'TASK_UPDATING';

export const TASKS_UPDATING_START = 'TASKS_UPDATING_START';
export const TASKS_LOADING_SUCCESS = 'TASKS_LOADING_SUCCESS';
export const TASK_ADDING_SUCCESS = 'TASK_ADDING_SUCCESS';
export const TASK_UPDATING_SUCCESS = 'TASK_UPDATING_SUCCESS';
export const TASKS_UPDATING_FAIL = 'TASKS_UPDATING_FAIL';

// Action Creators for Reducers
export const tasksUpdatingStartAC = () => ({type: TASKS_UPDATING_START});
export const tasksLoadingSuccessAC = tasks => ({type: TASKS_LOADING_SUCCESS, payload: {tasks}});
export const taskAddingSuccessAC = task => ({type: TASK_ADDING_SUCCESS, payload: {task}});
export const taskUpdatingSuccessAC = task => ({type: TASK_UPDATING_SUCCESS, payload: {task}});
export const tasksUpdatingFailAC = (errorMessage) => ({
  type: TASKS_UPDATING_FAIL,
  payload: {errorMessage}
});

// Action Creators for Sagas
export const loadTasksSagaAC = () => ({
  type: TASKS_LOADING
});
export const addTaskSagaAC = (todo) => ({
  type: TASK_ADDING,
  payload: {todo}
});
export const updateTaskSagaAC = (id, todo) => ({
  type: TASK_UPDATING,
  payload: {id, todo}
});