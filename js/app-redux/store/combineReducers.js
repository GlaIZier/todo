import {combineReducers} from 'redux';
import auth from '../redux/auth';
import register from '../redux/register';
import tasks from '../redux/task';
// import search from '../redux/search';
// import article from '../redux/article';
import notifications from '../redux/notifications';


const rootReducer = combineReducers({
  auth,
  register,
  tasks,
  // search,
  // articles,
  // article,
  notifications
});

export default rootReducer;