import {fromJS} from 'immutable';

const initialState = fromJS({
  tasks: [
    // {
    //   'id': '2febe14f-7e18-301d-b7e7-4b530dcccbec',
    //   'visibleOnline': true,
    //   'name': 'Title25',
    //   'doi': 'doi25',
    //   'publicationDate': '2015-12-15',
    //   'journal': 'journal25'
    // }
  ],
  // null if there are no next results
  nextPageOffset: null,
  loading: false,
  errorMessage: ''
});

// reducer
export default function reducer(tasks = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    // case TASKS_SEARCH_START:
    //   return tasks.set('loading', true).set('tasks', fromJS([])).set('nextPageOffset', null);

    case TASKS_PAGE_LOADING_START:
      return tasks.set('loading', true);

    // case TASKS_SEARCH_SUCCESS:
    //   return tasks.set('nextPageOffset', payload.tasks.nextPageOffset).set('tasks',
    //       fromJS(payload.tasks.tasks)).set('loading', false);

    case TASKS_PAGE_LOADING_SUCCESS:
      return tasks.set('nextPageOffset', payload.tasks.nextPageOffset).set('tasks',
        tasks.get('tasks').concat(fromJS(payload.tasks.tasks))).set('loading', false);

    case TASKS_PAGE_LOADING_FAIL:
      return tasks.set('loading', false).set('errorMessage', payload.errorMessage);

    default:
      return tasks;
  }
}

// Getters
export const getTasks = state => state.tasks.get('tasks').toJS();
export const getNextPageOffset = state => state.tasks.get('nextPageOffset');
export const getLoading = state => state.tasks.get('loading');

// constants
export const TASKS_PAGE_LOADING = 'TASKS_PAGE_LOADING';
export const TASKS_SEARCH_START = 'TASKS_SEARCH_START';
export const TASKS_PAGE_LOADING_START = 'TASKS_PAGE_LOADING_START';
export const TASKS_SEARCH_SUCCESS = 'TASKS_SEARCH_SUCCESS';
export const TASKS_PAGE_LOADING_SUCCESS = 'TASKS_PAGE_LOADING_SUCCESS';
export const TASKS_PAGE_LOADING_FAIL = 'TASKS_PAGE_LOADING_FAIL';

// Action Creators for Reducers
export const tasksSearchStartAC = () => ({type: TASKS_SEARCH_START});
export const tasksPageLoadingStartAC = () => ({type: TASKS_PAGE_LOADING_START});
export const tasksSearchSuccessAC = tasks => ({type: TASKS_SEARCH_SUCCESS, payload: {tasks}});
export const tasksPageLoadingSuccessAC = tasks => ({type: TASKS_PAGE_LOADING_SUCCESS, payload: {tasks}});
export const tasksPageLoadingFailAC = (errorMessage) => ({
  type: TASKS_PAGE_LOADING_FAIL,
  payload: {errorMessage}
});

// Action Creators for Sagas
export const loadNextPageSagaAC = () => ({
  type: TASKS_PAGE_LOADING
});