import commonConfig from './config.common';
import {AuthService} from '../services/Auth';
import {RegisterService} from '../services/Register';
import {TaskService} from '../services/Task';

const config = {...commonConfig};

export default {
  authService: new AuthService(config),
  registerService: new RegisterService(config),
  taskService: new TaskService(config)
};
