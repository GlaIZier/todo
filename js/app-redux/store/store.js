import {applyMiddleware, createStore} from 'redux';
import rootReducer from './combineReducers';
import {createLogger} from 'redux-logger';
import createSagaMiddleware from 'redux-saga';
import rootSaga from '../sagas';

// Middlewares
const loggerMiddleware = createLogger();
const sagaMiddleware = createSagaMiddleware();

let store;
if (process.env.NODE_ENV !== 'production') {
  store = createStore(
    rootReducer,
    {},
    applyMiddleware(sagaMiddleware, loggerMiddleware)
  );
} else {
  store = createStore(
    rootReducer,
    {},
    applyMiddleware(sagaMiddleware)
  );
}

// Start saga watch
sagaMiddleware.run(rootSaga);

// Export all store in console
window.store = store;

export default store