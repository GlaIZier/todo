import {watchLogin, watchLogout} from './authSaga';
import {watchRegister} from './registerSaga';
import {watchTasksLoading} from './taskSaga';
// import {watchArticlesPageLoading, watchSearch} from './searchSaga';
// import {
//     watchAddFoundConceptSaga,
//     watchArticleLoading,
//     watchConceptsPageLoading,
//     watchConceptsSearch,
//     watchLoadConceptChildrenSaga,
//     watchLoadTopConceptChildrenSaga,
//     watchRemoveArticleConceptSaga,
//     watchRemoveArticleHumanConceptSaga
// } from './articleSaga';
import {watchArticleNavigate, watchNavigate} from './routerSaga';
import {watchNotifyDanger, watchNotifyInfo, watchNotifySuccess, watchNotifyWarning} from './notificationsSaga';

export default function* rootSaga() {
  yield [
    watchLogin(),
    watchLogout(),

    watchRegister(),

    watchTasksLoading(),

    // watchSearch(),
    // watchArticlesPageLoading(),
    //
    // watchArticleLoading(),
    // watchConceptsSearch(),
    // watchConceptsPageLoading(),
    // watchRemoveArticleConceptSaga(),
    // watchRemoveArticleHumanConceptSaga(),
    // watchAddFoundConceptSaga(),
    //
    // watchLoadTopConceptChildrenSaga(),
    // watchLoadConceptChildrenSaga(),

    watchNavigate(),
    watchArticleNavigate(),

    watchNotifySuccess(),
    watchNotifyInfo(),
    watchNotifyWarning(),
    watchNotifyDanger()
  ];
}