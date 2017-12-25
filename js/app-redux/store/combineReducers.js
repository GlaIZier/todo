import {combineReducers} from 'redux';
import auth from '../redux/auth';
import tasks from '../redux/tasks';
// import search from '../redux/search';
// import article from '../redux/article';
import notifications from '../redux/notifications';


const rootReducer = combineReducers({
  auth,
  tasks,
  // search,
  // articles,
  // article,
  notifications
});

export default rootReducer;