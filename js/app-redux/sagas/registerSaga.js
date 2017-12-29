import {call, put, takeEvery} from 'redux-saga/effects';
import {REGISTER, registerFailAC, registerStartAC, registerSuccessAC} from '../redux/register';
import {navigateSagaAC} from '../redux/navigate';
import {notifyDangerSagaAC, notifySuccessSagaAC} from '../redux/notifications';
import Services from '../config/config.services';

export function* registerSaga(action) {
  try {
    const {payload: {login, password}} = action;

    // saga itself inside put function can dispatch actions to reducers
    yield put(registerStartAC());

    // indirect async call is used to help to test this middleware
    const payload = yield call(Services.registerService.register, login, password);

    yield put(registerSuccessAC(payload.data.login));
    yield put(notifySuccessSagaAC(`User with login '${payload.data.login}' has been created successfully!`));
    yield put(navigateSagaAC('root'));
  } catch (e) {
    console.error('Register error: ', e);
    if (e.responseJSON) {
      yield put(registerFailAC(e.responseJSON.error.message));
      yield put(notifyDangerSagaAC(`Logout failed: ${e.responseJSON.error.message}. Try one more time or reload the page!`));
    }
    else if (e.responseText) {
      yield put(registerFailAC(e.responseText));
      yield put(notifyDangerSagaAC(`Logout failed: ${e.responseText}. Try one more time or reload the page!`));
    }
    else if (e.message) {
      yield put(registerFailAC(e.message));
      yield put(notifyDangerSagaAC(`Logout failed: ${e.message}. Try one more time or reload the page!`));
    }
    else {
      yield put(registerFailAC('Unknown error occurred! Check console for more information.'));
      yield put(notifyDangerSagaAC('Unknown error occurred! Check console for more information.'));
    }
  }
}

export function* watchRegister() {
  yield takeEvery(REGISTER, registerSaga);
}

