import React from 'react';
import {render} from 'react-dom';
import {Provider} from 'react-redux';
import {AppRouter} from './router';
import store from './store/store';
import 'jquery';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min';
import './styles/main.css';

render(
  <Provider store={store}>
    <AppRouter/>
  </Provider>,
  document.getElementById('app')
);