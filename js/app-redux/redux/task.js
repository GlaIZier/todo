import {fromJS} from 'immutable';

const initialState = fromJS({
  task: {},
    // {
    //   "id": 1,
    //   "login": "u",
    //   "todo": "todo1"
    // }
  loading: false,
  errorMessage: null
});

// reducer
export default function reducer(task = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case TASK_UPDATING_START:
      return task.set('loading', true).set('errorMessage', null);

    case TASK_LOADING_SUCCESS:
      return task.set('task', fromJS(payload.task))
        .set('loading', false).set('errorMessage', null);

    case TASK_UPDATING_FAIL:
      return task.set('loading', false).set('errorMessage', payload.errorMessage);

    default:
      return task;
  }
}

// Getters
export const getTask = state => state.task.get('task').toJS();
export const getErrorMessage = state => state.task.get('errorMessage');
export const getLoading = state => state.task.get('loading');

// constants
export const TASK_LOADING = 'TASK_LOADING';

export const TASK_UPDATING_START = 'TASK_UPDATING_START';
export const TASK_LOADING_SUCCESS = 'TASK_LOADING_SUCCESS';
export const TASK_UPDATING_FAIL = 'TASK_UPDATING_FAIL';

// Action Creators for Reducers
export const taskUpdatingStartAC = () => ({type: TASK_UPDATING_START});
export const taskLoadingSuccessAC = task => ({type: TASK_LOADING_SUCCESS, payload: {task}});
export const taskUpdatingFailAC = (errorMessage) => ({
  type: TASK_UPDATING_FAIL,
  payload: {errorMessage}
});

// Action Creators for Sagas
export const loadTaskSagaAC = (taskId) => ({
  type: TASK_LOADING,
  payload: {taskId}
});