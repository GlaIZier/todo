import {call, put, select, takeEvery} from 'redux-saga/effects';
import {getFilters, getJournalIds, SEARCH, searchFailAC, searchStartAC, searchSuccessAC} from '../redux/search';
import {notifyDangerSagaAC, notifyInfoSagaAC} from '../redux/notifications';
import Services from '../config/config.services';

export function* searchSaga(action) {
  try {
    const {payload: {filters, offset}} = action;
    // saga itself inside put function can dispatch actions to reducers
    yield put(searchStartAC(filters));
    const allowedJournals = yield select(getAllowedJournals);

    let journalIds = [];
    if (filters.journal && filters.journal !== '') {
      const journalsResponse = yield call(Services.searchService.searchJournals, filters.journal);
      // console.log(JSON.stringify(journalsResponse, null, 2));
      journalIds = _getJournalIds(journalsResponse, allowedJournals);
      // console.log(JSON.stringify(journalIds, null, 2));

      if (journalIds.length === 0) {
        yield put(searchFailAC('No journals found'));
        yield put(notifyDangerSagaAC('No journal(s) with given title have been found'));
        return;
      }
    } else {
      journalIds = allowedJournals;
    }

    const articlesResponse = yield call(Services.searchService.searchArticles, journalIds, filters, offset);
    // console.log(JSON.stringify(articlesResponse, null, 2));

    // const response = yield call(Services.apiService.searchArticles, filters, offset);
    // console.log('Payload inside searchSaga() after SearchService.search() success: ' + JSON.stringify(response, null, 2));
    const parsedResponse = parseSearchResponse(articlesResponse);
    // console.log(JSON.stringify(parsedResponse, null, 2));
    // console.log('Parsed response: ' + JSON.stringify(parsedResponse, null, 2));

    for (let article of articlesResponse.content) {
      const articleConceptsResponse = yield call(Services.articleService.getArticleConcepts, article.id);
      article.concepts = articleConceptsResponse.content;
    }

    yield put(articlesSearchSuccessAC(parsedResponse));

    yield put(searchSuccessAC(journalIds));

    if (journalIds.length === 0) {
      yield put(notifyInfoSagaAC('No journals were specified. Searching in fresh articles since 1st of June, 2017'));
    }
  } catch (e) {
    console.error('Error: ', e);
    yield put(searchFailAC(e.message));
    yield put(notifyDangerSagaAC(`Searching failed: ${e.message}. Try one more time or reload the page!`));
    // yield call(errorHandlerSaga, e, 'sagas.login.failed', action);
  }
}

export function* loadNextPageSaga() {
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

export function* watchSearch() {
  yield takeEvery(SEARCH, searchSaga);
}

export function* watchArticlesPageLoading() {
  yield takeEvery(TASKS_LOADING, loadNextPageSaga);
}