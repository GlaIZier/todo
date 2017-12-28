import {Map} from 'immutable';

const initialState = new Map({
  hasError: false,
  errorMessage: '',
  processing: false,
  lastRegisteredLogin: null
});

// reducer
export default function reducer(register = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case REGISTER_START:
      return register.set('hasError', false).set('errorMessage', '').set('processing', true);

    case REGISTER_SUCCESS:
      return register.set('hasError', false).set('errorMessage', '').set('processing', false)
        .set('lastRegisteredLogin', payload.lastRegisteredLogin);

    case REGISTER_FAIL:
      return register.set('hasError', true).set('errorMessage', payload.errorMessage).set('processing', false);

    default:
      return register
  }
}

// Getters
export const getHasError = state => state.auth.get('hasError');
export const getErrorMessage = state => state.auth.get('errorMessage');
export const getProcessing = state => state.auth.get('processing');

// constants
export const REGISTER = 'REGISTER';
export const REGISTER_START = 'REGISTER_START';
export const REGISTER_SUCCESS = 'REGISTER_SUCCESS';
export const REGISTER_FAIL = 'REGISTER_FAIL';

// Action Creators for Reducers
export const registerStartAC = () => ({type: REGISTER_START, payload: {}});
export const registerFailAC = errorMessage => ({type: REGISTER_FAIL, payload: {errorMessage}});
export const registerSuccessAC = (lastRegisteredLogin) => ({type: REGISTER_SUCCESS, payload: {lastRegisteredLogin}});

// Action Creators for Sagas
export const registerSagaAC = ({login, password}) => ({
  type: REGISTER,
  payload: {login, password},
});
