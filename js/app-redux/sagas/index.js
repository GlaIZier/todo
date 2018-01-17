import {watchLogin, watchLogout} from './authSaga';
import {watchRegister} from './registerSaga';
import {watchTaskAdding, watchTaskDeleting, watchTaskLoading, watchTasksLoading, watchTaskUpdating} from './taskSaga';
import {watchNavigate} from './routerSaga';
import {watchNotifyDanger, watchNotifyInfo, watchNotifySuccess, watchNotifyWarning} from './notificationsSaga';

export default function* rootSaga() {
  yield [
    watchLogin(),
    watchLogout(),

    watchRegister(),

    watchTasksLoading(),
    watchTaskAdding(),
    watchTaskUpdating(),
    watchTaskDeleting(),
    watchTaskLoading(),

    watchNavigate(),

    watchNotifySuccess(),
    watchNotifyInfo(),
    watchNotifyWarning(),
    watchNotifyDanger()
  ];
}