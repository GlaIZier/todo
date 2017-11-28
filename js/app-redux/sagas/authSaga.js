import {call, put, takeEvery} from 'redux-saga/effects';
import {LOGIN, loginFailAC, loginStartAC, loginSuccessAC, LOGOUT, logoutSuccessAC} from '../redux/auth';
import {navigateSagaAC} from '../redux/navigate';
import {notifyDangerSagaAC} from '../redux/notifications';
import Services from '../config/config.services';
// import { errorHandlerSaga } from './errorHandlerSaga';

export function* loginSaga(action) {
  try {
    const {payload: {username, password}} = action;

    // saga itself inside put function can dispatch actions to reducers
    yield put(loginStartAC());
    // indirect async call is used to help to test this middleware
    const payload = yield call(Services.authService.login, username, password);
    console.debug('Payload inside loginSaga() after AuthService.login() success: ' + payload);
    window.localStorage.setItem('todo-user', JSON.stringify(payload.payload));

    yield put(loginSuccessAC(payload.payload));
    yield put(navigateSagaAC('articles'));
  } catch (e) {
    console.error('Login error: ', e);
    yield put(loginFailAC(e.responseJSON.message));
    // yield call(errorHandlerSaga, e, 'sagas.login.failed', action);
  }
}

export function* logoutSaga() {
  try {
    yield call(Services.authService.logout);
    window.localStorage.removeItem('cc-user');
    window.localStorage.removeItem('cc-user-journals');
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
