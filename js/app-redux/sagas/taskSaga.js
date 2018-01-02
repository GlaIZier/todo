import {call, put, takeEvery} from 'redux-saga/effects';
import {
  TASKS_LOADING,
  TASK_ADDING,
  tasksUpdatingStartAC,
  tasksLoadingSuccessAC,
  taskAddingSuccessAC,
  tasksUpdatingFailAC
} from '../redux/task';
import {notifyDangerSagaAC} from '../redux/notifications';
import Services from '../config/config.services';

// Todo add to functions navigate to login if 401 or 403 to catch
export function* loadTasksSaga() {
  try {
    yield put(tasksUpdatingStartAC());

    const tasksResponse = yield call(Services.taskService.loadTasks);
    const tasks = parseTasksResponse(tasksResponse);

    yield put(tasksLoadingSuccessAC(tasks));
  } catch (e) {
    console.error('Error: ', e);
    if (e.responseJSON) {
      yield put(tasksUpdatingFailAC(e.responseJSON.error.message));
      yield put(notifyDangerSagaAC(`Loading tasks failed: ${e.responseJSON.error.message}. Try one more time or reload the page!`));
    }
    else if (e.responseText) {
      yield put(tasksUpdatingFailAC(e.responseText));
      yield put(notifyDangerSagaAC(`Loading tasks failed: ${e.responseText}. Try one more time or reload the page!`));
    }
    else if (e.message) {
      yield put(tasksUpdatingFailAC(e.message));
      yield put(notifyDangerSagaAC(`Loading tasks failed: ${e.message}. Try one more time or reload the page!`));
    }
    else {
      yield put(tasksUpdatingFailAC('Unknown error occurred! Check console for more information.'));
      yield put(notifyDangerSagaAC('Unknown error occurred! Check console for more information.'));
    }
  }
}

export function* addTaskSaga(action) {
  try {
    const {payload: {todo}} = action;

    yield put(tasksUpdatingStartAC());

    const addedTaskResponse = yield call(Services.taskService.addTask, todo);
    const addedTask = addedTaskResponse.data;

    yield put(taskAddingSuccessAC(addedTask));
  } catch (e) {
    console.error('Error: ', e);
    if (e.responseJSON) {
      yield put(tasksUpdatingFailAC(e.responseJSON.error.message));
      yield put(notifyDangerSagaAC(`Adding task failed: ${e.responseJSON.error.message}. Try one more time or reload the page!`));
    }
    else if (e.responseText) {
      yield put(tasksUpdatingFailAC(e.responseText));
      yield put(notifyDangerSagaAC(`Adding task failed: ${e.responseText}. Try one more time or reload the page!`));
    }
    else if (e.message) {
      yield put(tasksUpdatingFailAC(e.message));
      yield put(notifyDangerSagaAC(`Adding task failed: ${e.message}. Try one more time or reload the page!`));
    }
    else {
      yield put(tasksUpdatingFailAC('Unknown error occurred! Check console for more information.'));
      yield put(notifyDangerSagaAC('Unknown error occurred! Check console for more information.'));
    }
  }
}

export function* editTaskSaga() {

}

export function* deleteTaskSaga() {

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

export function* watchTaskAdding() {
  yield takeEvery(TASK_ADDING, addTaskSaga);
}