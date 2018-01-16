import {call, put, takeEvery} from 'redux-saga/effects';
import {
  TASK_ADDING,
  TASK_DELETING,
  TASK_UPDATING,
  taskAddingSuccessAC,
  taskDeletingSuccessAC,
  TASKS_LOADING,
  tasksLoadingSuccessAC,
  tasksUpdatingFailAC,
  tasksUpdatingStartAC,
  taskUpdatingSuccessAC
} from '../redux/tasks';

import {TASK_LOADING, taskLoadingSuccessAC, taskUpdatingFailAC, taskUpdatingStartAC} from '../redux/task';
import {notifyDangerSagaAC} from '../redux/notifications';
import Services from '../config/config.services';

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

export function* updateTaskSaga(action) {
  try {
    const {payload: {id, todo}} = action;

    yield put(tasksUpdatingStartAC());

    const updatedTaskResponse = yield call(Services.taskService.updateTask, id, todo);
    const updatedTask = updatedTaskResponse.data;

    yield put(taskUpdatingSuccessAC(updatedTask));
  } catch (e) {
    console.error('Error: ', e);
    if (e.responseJSON) {
      yield put(tasksUpdatingFailAC(e.responseJSON.error.message));
      yield put(notifyDangerSagaAC(`Updating task failed: ${e.responseJSON.error.message}. Try one more time or reload the page!`));
    }
    else if (e.responseText) {
      yield put(tasksUpdatingFailAC(e.responseText));
      yield put(notifyDangerSagaAC(`Updating task failed: ${e.responseText}. Try one more time or reload the page!`));
    }
    else if (e.message) {
      yield put(tasksUpdatingFailAC(e.message));
      yield put(notifyDangerSagaAC(`Updating task failed: ${e.message}. Try one more time or reload the page!`));
    }
    else {
      yield put(tasksUpdatingFailAC('Unknown error occurred! Check console for more information.'));
      yield put(notifyDangerSagaAC('Unknown error occurred! Check console for more information.'));
    }
  }
}

export function* deleteTaskSaga(action) {
  try {
    const {payload: {id}} = action;

    yield put(tasksUpdatingStartAC());

    const deletedTaskResponse = yield call(Services.taskService.deleteTask, id);
    const deletedTask = deletedTaskResponse.data;

    yield put(taskDeletingSuccessAC(deletedTask.id));
  } catch (e) {
    console.error('Error: ', e);
    if (e.responseJSON) {
      yield put(tasksUpdatingFailAC(e.responseJSON.error.message));
      yield put(notifyDangerSagaAC(`Deleting task failed: ${e.responseJSON.error.message}. Try one more time or reload the page!`));
    }
    else if (e.responseText) {
      yield put(tasksUpdatingFailAC(e.responseText));
      yield put(notifyDangerSagaAC(`Deleting task failed: ${e.responseText}. Try one more time or reload the page!`));
    }
    else if (e.message) {
      yield put(tasksUpdatingFailAC(e.message));
      yield put(notifyDangerSagaAC(`Deleting task failed: ${e.message}. Try one more time or reload the page!`));
    }
    else {
      yield put(tasksUpdatingFailAC('Unknown error occurred! Check console for more information.'));
      yield put(notifyDangerSagaAC('Unknown error occurred! Check console for more information.'));
    }
  }
}

export function* loadTaskSaga(action) {
  try {
    const {payload: {taskId}} = action;
    yield put(taskUpdatingStartAC());

    const tasksResponse = yield call(Services.taskService.loadTask, taskId);
    yield put(taskLoadingSuccessAC(tasksResponse.data));
  } catch (e) {
    console.error('Error: ', e);
    if (e.responseJSON) {
      yield put(taskUpdatingFailAC(e.responseJSON.error.message));
      yield put(notifyDangerSagaAC(`Loading task failed: ${e.responseJSON.error.message}. Try one more time or reload the page!`));
    }
    else if (e.responseText) {
      yield put(taskUpdatingFailAC(e.responseText));
      yield put(notifyDangerSagaAC(`Loading task failed: ${e.responseText}. Try one more time or reload the page!`));
    }
    else if (e.message) {
      yield put(taskUpdatingFailAC(e.message));
      yield put(notifyDangerSagaAC(`Loading task failed: ${e.message}. Try one more time or reload the page!`));
    }
    else {
      yield put(taskUpdatingFailAC('Unknown error occurred! Check console for more information.'));
      yield put(notifyDangerSagaAC('Unknown error occurred! Check console for more information.'));
    }
  }
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

export function* watchTaskUpdating() {
  yield takeEvery(TASK_UPDATING, updateTaskSaga);
}

export function* watchTaskDeleting() {
  yield takeEvery(TASK_DELETING, deleteTaskSaga);
}

export function* watchTaskLoading() {
  yield takeEvery(TASK_LOADING, loadTaskSaga);
}