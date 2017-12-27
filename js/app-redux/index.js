import React from 'react';
import {render} from 'react-dom';
import {Provider} from 'react-redux';
import {AppRouter} from './router';
import store from './store/store';
import './styles/bootstrap.min.css';
import './styles/main.css';

// Todo remove all unnecessary components
render(
  <Provider store={store}>
    <AppRouter />
  </Provider>,
  document.getElementById('app')
);