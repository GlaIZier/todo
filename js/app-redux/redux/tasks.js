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
  errorMessage: ''
});

// reducer
export default function reducer(tasks = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case TASKS_LOADING_START:
      return tasks.set('loading', true);

    case TASKS_LOADING_SUCCESS:
      return tasks.set('nextPageOffset', payload.tasks.nextPageOffset).set('tasks',
        tasks.get('tasks').concat(fromJS(payload.tasks.tasks))).set('loading', false);

    case TASKS_LOADING_FAIL:
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
export const TASKS_LOADING_START = 'TASKS_LOADING_START';
export const TASKS_LOADING_SUCCESS = 'TASKS_LOADING_SUCCESS';
export const TASKS_LOADING_FAIL = 'TASKS_LOADING_FAIL';

// Action Creators for Reducers
export const tasksLoadingStartAC = () => ({type: TASKS_LOADING_START});
export const tasksLoadingSuccessAC = tasks => ({type: TASKS_LOADING_SUCCESS, payload: {tasks}});
export const tasksLoadingFailAC = (errorMessage) => ({
  type: TASKS_LOADING_FAIL,
  payload: {errorMessage}
});

// Action Creators for Sagas
export const loadTasksSagaAC = () => ({
  type: TASKS_LOADING
});