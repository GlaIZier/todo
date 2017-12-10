import {call, put, takeEvery} from 'redux-saga/effects';
import {LOGIN, loginFailAC, loginStartAC, loginSuccessAC, LOGOUT, logoutSuccessAC} from '../redux/auth';
import {navigateSagaAC} from '../redux/navigate';
import {notifyDangerSagaAC} from '../redux/notifications';
import Services from '../config/config.services';
import Cookies from 'js-cookie';
import config from '../config/config.common';


// import { errorHandlerSaga } from './errorHandlerSaga';

export function* loginSaga(action) {
  try {
    const {payload: {login, password}} = action;

    // saga itself inside put function can dispatch actions to reducers
    yield put(loginStartAC());

    // indirect async call is used to help to test this middleware
    const payload = yield call(Services.authService.login, login, password);
    console.debug('Payload inside loginSaga() after AuthService.login() success: ' + payload);

    let user = payload.data;
    window.localStorage.setItem(config.constants.localStorageUserItemName, JSON.stringify(user));
    Cookies.set(config.constants.apiTokenCookieName, user.token, {expires: config.constants.apiTokenExpireDays});


    yield put(loginSuccessAC(user));
    yield put(navigateSagaAC('tasks'));
  } catch (e) {
    console.error('Login error: ', e);
    if (e.responseJSON)
      yield put(loginFailAC(e.responseJSON.error.message));
    else if (e.responseText)
      yield put(loginFailAC(e.responseText));
    else if (e.message)
      yield put(loginFailAC(e.message));
    else
      yield put(loginFailAC('Unknown error occurred! Check console for more information.'));
    // yield call(errorHandlerSaga, e, 'sagas.login.failed', action);
  }
}

export function* logoutSaga() {
  try {
    const token = Cookies.get(config.constants.apiTokenCookieName);
    yield call(Services.authService.logout, token);

    window.localStorage.removeItem('todo-user');
    Cookies.remove(config.constants.apiTokenCookieName);

    yield put(logoutSuccessAC());
  } catch (e) {
    console.error(e);
    if (e.responseJSON)
      yield put(notifyDangerSagaAC(`Logout failed: ${e.responseJSON.error.message}. Try one more time or reload the page!`));
    if (e.responseText)
      yield put(notifyDangerSagaAC(`Logout failed: ${e.responseText}. Try one more time or reload the page!`));
    else if (e.message)
      yield put(notifyDangerSagaAC(`Logout failed: ${e.message}. Try one more time or reload the page!`));
    else
      yield put(notifyDangerSagaAC('Unknown error occurred! Check console for more information.'));

  }
}

export function* watchLogin() {
  yield takeEvery(LOGIN, loginSaga);
}

export function* watchLogout() {
  yield takeEvery(LOGOUT, logoutSaga);
}
