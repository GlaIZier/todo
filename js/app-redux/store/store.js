import {applyMiddleware, createStore} from 'redux';
import rootReducer from './combineReducers';
import {createLogger} from 'redux-logger';
import createSagaMiddleware from 'redux-saga';
import rootSaga from '../sagas';

// Middlewares
const loggerMiddleware = createLogger();
const sagaMiddleware = createSagaMiddleware();

let store;

if (!__PROD__) {
  store = createStore(
    rootReducer,
    {},
    applyMiddleware(sagaMiddleware, loggerMiddleware)
  );
  console.log('Non prod profile has been found in store. Saga and logger middleware have been added');
} else {
  store = createStore(
    rootReducer,
    {},
    applyMiddleware(sagaMiddleware)
  );
  console.log('Prod profile has been found in store. Saga middleware without logger has been added')
}

// Start saga watch
sagaMiddleware.run(rootSaga);

// Export all store in console
if (!__PROD__) {
  window.store = store;
  console.log('Store has been exported to windows.store for non prod profile')
}

export default store