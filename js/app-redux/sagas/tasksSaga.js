import {call, put, select, takeEvery} from 'redux-saga/effects';
import {getErrorMessage, TASKS_LOADING} from '../redux/tasks';
import {notifyDangerSagaAC} from '../redux/notifications';
import Services from '../config/config.services';

export function* loadTasksSaga() {
  try {
    yield put(articlesPageLoadingStartAC());

    const filters = yield select(getFilters);
    const journalIds = yield select(getJournalIds);
    const nextPageOffset = yield select(getErrorMessage);

    const articlesResponse = yield call(Services.searchService.searchArticles, journalIds, filters, nextPageOffset);
    // const response = yield call(Services.apiService.searchArticles, filters, nextPageOffset);
    const parsedResponse = parseSearchResponse(articlesResponse);

    for (let article of articlesResponse.content) {
      const articleConceptsResponse = yield call(Services.articleService.getArticleConcepts, article.id);
      article.concepts = articleConceptsResponse.content;
    }

    yield put(articlesPageLoadingSuccessAC(parsedResponse));
  } catch (e) {
    console.error('Error: ', e);
    // yield put(articlesPageLoadingFailAC(e.message));
    yield put(notifyDangerSagaAC(`Loading next page failed: ${e.message}. Try to reload the page.`));
  }
}

function _getJournalIds(response, allowedJournals) {
  const content = response.content;
  // allow all journals if no allowed journals specified
  let journalIds = [];
  if (content) {
    for (let contentElement of content) {
      if ((!allowedJournals || allowedJournals.length === 0)
        || (allowedJournals && allowedJournals.length > 0 && allowedJournals.indexOf(contentElement.id) !== -1)) {
        journalIds.push(contentElement.id);
      }
    }
  }
  return journalIds;
}

function parseSearchResponse(response) {
  let parsedResponse = {
    articles: [],
    nextPageOffset: null
  };

  if (response.links.next) {
    // This works only in modern browsers Chrome 49+
    // parsedResponse.nextPageOffset = new URL(response.links.next).searchParams.get('offset');
    parsedResponse.nextPageOffset = Number(parseGetParam('offset', response.links.next));
  }

  if (response.content) {
    parsedResponse.articles = response.content;
  }

  return parsedResponse;
}

function parseGetParam(name, url) {
  if (!url) return null;

  name = name.replace(/[\[\]]/g, '\\$&');
  let regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

export function* watchTasksLoading() {
  yield takeEvery(TASKS_LOADING, loadTasksSaga);
}