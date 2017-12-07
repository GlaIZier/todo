import {call, put, takeEvery} from 'redux-saga/effects';
import {LOGIN, loginFailAC, loginStartAC, loginSuccessAC, LOGOUT, logoutSuccessAC} from '../redux/auth';
import {navigateSagaAC} from '../redux/navigate';
import {notifyDangerSagaAC} from '../redux/notifications';
import Services from '../config/config.services';
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
    window.localStorage.setItem('todo-user', JSON.stringify(user));

    yield put(loginSuccessAC(user));
    yield put(navigateSagaAC('tasks'));
  } catch (e) {
    console.error('Login error: ', e);
    if (!e.responseJSON)
      yield put(loginFailAC('Unknown error occurred! Check console for more information.'));
    else
      yield put(loginFailAC(e.responseJSON.error.message));
    // yield call(errorHandlerSaga, e, 'sagas.login.failed', action);
  }
}

export function* logoutSaga() {
  try {
    yield call(Services.authService.logout);
    window.localStorage.removeItem('todo-user');
    yield put(logoutSuccessAC());
  } catch (e) {
    console.error(e);
    yield put(notifyDangerSagaAC(`Logout failed: ${e.message}. Try one more time or reload the page!`));
  }
}

export function* watchLogin() {
  yield takeEvery(LOGIN, loginSaga);
}

export function* watchLogout() {
  yield takeEvery(LOGOUT, logoutSaga);
}
