import {call, put, takeEvery} from 'redux-saga/effects';
import {TASKS_LOADING, tasksLoadingStartAC, tasksLoadingSuccessAC} from '../redux/task';
import {notifyDangerSagaAC} from '../redux/notifications';
import Services from '../config/config.services';

export function* loadTasksSaga() {
  try {
    yield put(tasksLoadingStartAC());

    const tasksResponse = yield call(Services.taskService.loadTasks);
    const tasks = parseTasksResponse(tasksResponse);

    yield put(tasksLoadingSuccessAC(tasks));
  } catch (e) {
    console.error('Error: ', e);
    // yield put(articlesPageLoadingFailAC(e.message));
    yield put(notifyDangerSagaAC(`Loading next page failed: ${e.message}. Try to reload the page.`));
  }
}

export function* addTaskSaga() {

}


function parseTasksResponse(response) {
  let tasks = {
    tasks: []
  };

  if (response.data.length > 0) {
    for (let task of response.data) {
      tasks.tasks.push(task.data)
    }
  }

  return tasks;
}

export function* watchTasksLoading() {
  yield takeEvery(TASKS_LOADING, loadTasksSaga);
}