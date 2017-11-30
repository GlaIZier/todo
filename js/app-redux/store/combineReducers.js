import {combineReducers} from 'redux';
import auth from '../redux/auth';
// import search from '../redux/search';
// import articles from '../redux/articles';
// import article from '../redux/article';
import notifications from '../redux/notifications';


const rootReducer = combineReducers({
  auth,
  // search,
  // articles,
  // article,
  notifications
});

export default rootReducer;