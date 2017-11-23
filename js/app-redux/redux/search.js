import {fromJS} from 'immutable';

const initialState = fromJS({
  hasError: false,
  errorMessage: '',
  processing: false,
  /**
   "filter": {
    "journal": String, // isn't used right now
    "status": String,
    "doi": String,
    "name": String,
    "sort": String
   */
  filters: {
    journal: '',
    status: 'all',
    doi: '',
    name: '',
    sort: 'calc'
  },
  // journalIds to find articles in
  journalIds: []
});

// reducer
export default function reducer(search = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case SEARCH_START:
      return search.set('hasError', false).set('errorMessage', '').set('processing', true).setIn(['filters'], fromJS(payload.filters))
        .setIn(['journalIds'], fromJS([]));

    case SEARCH_SUCCESS:
      return search.set('hasError', false).set('errorMessage', '').set('processing', false).setIn(['journalIds'], fromJS(payload.journalIds));

    case SEARCH_FAIL:
      return search.set('hasError', true).set('errorMessage', payload.errorMessage).set('processing', false);

    default:
      return search
  }
}

// Getters
export const getHasError = state => state.search.get('hasError');
export const getErrorMessage = state => state.search.get('errorMessage');
export const getProcessing = state => state.search.get('processing');
export const getFilters = state => state.search.get('filters').toJS();
export const getJournalIds = state => state.search.get('journalIds').toJS();
export const getSearchState = state => state.search;

// constants
export const SEARCH = 'SEARCH';
export const SEARCH_START = 'SEARCH_START';
export const SEARCH_SUCCESS = 'SEARCH_SUCCESS';
export const SEARCH_FAIL = 'SEARCH_FAIL';

// Action Creators for Reducers
export const searchStartAC = filters => ({type: SEARCH_START, payload: {filters}});
export const searchFailAC = errorMessage => ({type: SEARCH_FAIL, payload: {errorMessage}});
export const searchSuccessAC = journalIds => ({type: SEARCH_SUCCESS, payload: {journalIds}});

// Action Creators for Sagas
export const searchSagaAC = (filters, offset) => ({
  type: SEARCH,
  payload: {filters, offset},
});