import {combineReducers} from 'redux';
import auth from '../redux/auth';
import register from '../redux/register';
import tasks from '../redux/tasks';
import task from '../redux/task';
import notifications from '../redux/notifications';


const rootReducer = combineReducers({
  auth,
  register,
  tasks,
  task,
  notifications
});

export default rootReducer;