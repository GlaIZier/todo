import {Map, fromJS} from 'immutable';

const initialState = new Map({
  hasError: false,
  errorMessage: '',
  processing: false,
  journals: fromJS(JSON.parse(window.localStorage.getItem('cc-user-journals'))),
  /**
   "user": {
    "email": {
      "present": true
    },
    "name": {
      "present": true
    },
    "token": {
      "present": true
    },
    "username": {
      "present": true
   }
   */
  user: JSON.parse(window.localStorage.getItem('cc-user')),
});

// reducer
export default function reducer(auth = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case LOGIN_START:
      return auth.set('hasError', false).set('errorMessage', '').set('processing', true);

    case LOGIN_SUCCESS:
      return auth.set('hasError', false).set('errorMessage', '').set('processing', false)
          .set('user', payload.user).setIn(['journals'], fromJS(payload.journals));

    case LOGIN_FAIL:
      return auth.set('hasError', true).set('errorMessage', payload.errorMessage).set('processing', false);

    case LOGOUT_SUCCESS:
      return auth.set('hasError', false).set('errorMessage', '').set('processing', false)
          .set('user', null).setIn(['journals'], []);

    default:
      return auth
  }
}

// Getters
export const getHasError = state => state.auth.get('hasError');
export const getErrorMessage = state => state.auth.get('errorMessage');
export const getProcessing = state => state.auth.get('processing');
export const getUser = state => state.auth.get('user');
export const getAllowedJournals = state => state.auth.get('journals') ? state.auth.get('journals').toJS() : [];

// constants
export const LOGIN = 'LOGIN';
export const LOGIN_START = 'LOGIN_START';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAIL = 'LOGIN_FAIL';
export const LOGOUT = 'LOGOUT';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';

// Action Creators for Reducers
export const loginStartAC = () => ({type: LOGIN_START, payload: {}});
export const loginFailAC = errorMessage => ({type: LOGIN_FAIL, payload: {errorMessage}});
export const loginSuccessAC = (user, journals) => ({type: LOGIN_SUCCESS, payload: {user, journals}});
export const logoutSuccessAC = () => ({type: LOGOUT_SUCCESS, payload: {}});

// Action Creators for Sagas
export const loginSagaAC = ({username, password}) => ({
  type: LOGIN,
  payload: {username, password},
});
export const logoutSagaAC = () => ({type: LOGOUT, payload: {}});
