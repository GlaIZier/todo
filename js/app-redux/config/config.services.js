import commonConfig from './config.common';
import {AuthService} from '../services/Auth';
import {RegisterService} from '../services/Register';
import {TaskService} from '../services/Task';
import {SearchService} from '../services/Search';
import {ArticleService} from '../services/Article';

const config = {...commonConfig};

export default {
  authService: new AuthService(config),
  registerService: new RegisterService(config),
  taskService: new TaskService(config),
  searchService: new SearchService(config),
  articleService: new ArticleService(config)
};
