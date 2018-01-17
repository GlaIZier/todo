import {call, put, takeEvery} from 'redux-saga/effects';
import {navigate} from '../router/navigate';
import {notifyDangerSagaAC} from '../redux/notifications';
import {ROUTE_NAVIGATE} from '../redux/navigate';

export function* navigateSaga(action) {
  try {
    const {path, props} = action.payload;
    yield call(navigate, path, props);
  } catch (e) {
    console.error(e);
    yield put(notifyDangerSagaAC(`Navigation to page failed: ${e.message}. Try to reload the page!`));
  }
}


// watchers
export function* watchNavigate() {
  yield takeEvery(ROUTE_NAVIGATE, navigateSaga);
}