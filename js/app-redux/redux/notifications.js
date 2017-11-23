import {fromJS} from 'immutable';

const initialState = fromJS({
  notifications: [
    // {
    //   id: 'string',
    //   message: 'string',
    //   // success, info, warning, danger
    //   bgStyle: 'danger'
    // }
  ]
});

// reducer
export default function reducer(notifications = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case NOTIFICATIONS_UPDATE:
      return notifications.set('notifications', fromJS(payload.notifications));

    default:
      return notifications;
  }
}

// Getters
export const getNotifications = state => state.notifications.get('notifications').toJS();

// constants
export const NOTIFY_SUCCESS = 'NOTIFY_SUCCESS';
export const NOTIFY_INFO = 'NOTIFY_INFO';
export const NOTIFY_WARNING = 'NOTIFY_WARNING';
export const NOTIFY_DANGER = 'NOTIFY_DANGER';
export const NOTIFICATIONS_UPDATE = 'NOTIFICATIONS_UPDATE';

// Action Creators for Reducers
export const updateNotificationsAC = (notifications) => ({
  type: NOTIFICATIONS_UPDATE,
  payload: {notifications}
});

// Action Creators for Sagas
export const notifySuccessSagaAC = (message) => ({
  type: NOTIFY_SUCCESS,
  payload: {message},
});
export const notifyInfoSagaAC = (message) => ({
  type: NOTIFY_INFO,
  payload: {message},
});
export const notifyWarningSagaAC = (message) => ({
  type: NOTIFY_WARNING,
  payload: {message},
});
export const notifyDangerSagaAC = (message) => ({
  type: NOTIFY_DANGER,
  payload: {message},
});