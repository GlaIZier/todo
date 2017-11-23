import {call, put, select, takeEvery} from 'redux-saga/effects';
import {
  getNotifications,
  NOTIFY_DANGER,
  NOTIFY_INFO,
  NOTIFY_SUCCESS,
  NOTIFY_WARNING,
  updateNotificationsAC
} from '../redux/notifications';
import uuid from 'uuid';
import commonConfig from '../config/config.common';
import Q from 'q';

export function* notifySuccessSaga(action) {
  try {
    const {payload: {message}} = action;
    yield call(_notify, message, 'success');
  } catch (e) {
    console.error(e);
  }
}

export function* notifyInfoSaga(action) {
  try {
    const {payload: {message}} = action;
    yield call(_notify, message, 'info');
  } catch (e) {
    console.error(e);
  }
}

export function* notifyWarningSaga(action) {
  try {
    const {payload: {message}} = action;
    yield call(_notify, message, 'warning');
  } catch (e) {
    console.error(e);
  }
}

export function* notifyDangerSaga(action) {
  try {
    const {payload: {message}} = action;
    yield call(_notify, message, 'danger');
  } catch (e) {
    console.error(e);
  }
}

function* _notify(message = '', bgStyle = '') {
  const id = yield call(_addNotification, message, bgStyle);
  yield call(_waitNotificationTimeout, commonConfig.constants.notificationTimeout);
  yield call(_removeNotification, id);
}

function* _addNotification(message = '', bgStyle = '') {
  const notifications = yield select(getNotifications);
  const id = uuid.v1();
  const notification = {
    id: id,
    message: message,
    bgStyle: bgStyle
  };
  notifications.push(notification);
  yield put(updateNotificationsAC(notifications));
  return id;
}

function _waitNotificationTimeout(timeout = 1000) {
  const defer = Q.defer();
  setTimeout(() => {
    return defer.resolve();
  }, timeout);
  return defer.promise;
}

function* _removeNotification(id = '') {
  const notifications = yield select(getNotifications);
  const editedNotifications = [];
  for (const notification of notifications) {
    if (notification.id !== id) {
      editedNotifications.push(notification);
    }
  }
  yield put(updateNotificationsAC(editedNotifications));
}

export function* watchNotifySuccess() {
  yield takeEvery(NOTIFY_SUCCESS, notifySuccessSaga);
}
export function* watchNotifyInfo() {
  yield takeEvery(NOTIFY_INFO, notifyInfoSaga);
}
export function* watchNotifyWarning() {
  yield takeEvery(NOTIFY_WARNING, notifyWarningSaga);
}
export function* watchNotifyDanger() {
  yield takeEvery(NOTIFY_DANGER, notifyDangerSaga);
}