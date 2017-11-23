import {call, put, select, takeEvery} from 'redux-saga/effects';
import {
    ARTICLE_CONCEPT_REMOVING,
    ARTICLE_HUMAN_CONCEPT_REMOVING,
    ARTICLE_LOADING,
    articleConceptsUpdatingSuccessAC,
    articleLoadingFailureAC,
    articleLoadingStartAC,
    articleLoadingSuccessAC,
    conceptNodeChildrenLoadingStartAC,
    conceptNodeChildrenLoadingSuccessAC,
    conceptNodeChildrenLoadingFailureAC,
    CONCEPTS_PAGE_LOADING,
    CONCEPTS_SEARCH,
    conceptsPageLoadingSuccessAC,
    conceptsSearchFailAC,
    conceptsSearchStartAC,
    conceptsSearchSuccessAC,
    conceptUpdateFinishAC,
    conceptUpdateStartAC,
    FOUND_CONCEPT_ADDITION,
    getConceptFilter,
    getConcepts,
    getNextPageOffset,
    getTaxonomies,
    LOAD_TOP_CONCEPT_CHILDREN, LOAD_CONCEPT_CHILDREN
} from '../redux/article';
import {notifyDangerSagaAC, notifySuccessSagaAC} from '../redux/notifications';
import {navigateSagaAC} from '../redux/navigate';
import Services from '../config/config.services';
import Util from '../util';
import {getAllowedJournals} from '../redux/auth';

export function* articleLoadingSaga(action) {
    const {payload: {id}} = action;
    try {
        yield put(articleLoadingStartAC());

        const article = yield call(Services.articleService.getArticleInfo, id);
        if (_isArticleInfoNotFound(article)) {
            yield put(navigateSagaAC('404'));
            return;
        }

        const articleJournalResponse = yield call(Services.articleService.getArticleJournal, id);
        const journal = articleJournalResponse.content[0];

        let taxonomies = [];
        if (journal && journal.id) {
            const allowedJournals = yield select(getAllowedJournals);

            if (allowedJournals && allowedJournals.length > 0 && allowedJournals.indexOf(journal.id) === -1) {
                yield put(navigateSagaAC('403'));
                return;
            }

            const journalTaxonomiesResponse = yield call(Services.articleService.getJournalTaxonomies, journal.id);
            taxonomies = _parseContentFromResponse(journalTaxonomiesResponse);

            const journalPolicy = yield call(Services.articleService.getJournalPolicy, journal.id);
            journal.min = journalPolicy.min;
            journal.max = journalPolicy.max;
        }

        const articleConceptsResponse = yield call(Services.articleService.getArticleConcepts, id);
        const concepts = _parseContentFromResponse(articleConceptsResponse);

        const articleHumanConceptsResponse = yield call(Services.articleService.getArticleHumanConcepts, id);
        const humanConcepts = _parseContentFromResponse(articleHumanConceptsResponse);

        yield put(articleLoadingSuccessAC(article, journal, taxonomies, concepts, humanConcepts));
    } catch (e) {
        console.error(e);
        yield put(articleLoadingFailureAC());
        if (e.status === 404) {
            yield put(navigateSagaAC('404'));
            return;
        }
        yield put(notifyDangerSagaAC(`Loading page failed: ${e.message}.`));
    }
}

export function* conceptsSearchSaga(action) {
    const {payload: {conceptFilter}} = action;
    try {
        yield put(conceptsSearchStartAC(conceptFilter));

        const taxonomies = yield select(getTaxonomies);

        const foundConceptsResponse = yield call(Services.articleService.searchConcepts, taxonomies, conceptFilter);

        const nextPageOffset = _parseNextPageOffsetFromResponse(foundConceptsResponse);
        const foundConcepts = _parseContentFromResponse(foundConceptsResponse);

        yield put(conceptsSearchSuccessAC(foundConcepts, nextPageOffset));
    } catch (e) {
        console.error(e);
        yield put(notifyDangerSagaAC(`Searching failed: ${e.message}. Try one more time or reload the page!`));
        yield put(conceptsSearchFailAC());
    }
}

export function* conceptsLoadNextPageSaga() {
    try {
        const conceptFilter = yield select(getConceptFilter);
        const offset = yield select(getNextPageOffset);
        const taxonomies = yield select(getTaxonomies);

        const response = yield call(Services.articleService.searchConcepts, taxonomies, conceptFilter, offset);
        // const response = yield call(Services.apiService.searchArticles, filters, nextPageOffset);
        const nextPageOffset = _parseNextPageOffsetFromResponse(response);
        const foundConcepts = _parseContentFromResponse(response);

        yield put(conceptsPageLoadingSuccessAC(foundConcepts, nextPageOffset));
    } catch (e) {
        console.error('Error: ', e);
        yield put(notifyDangerSagaAC(`Loading next page failed: ${e.message}. Try to reload the page.`));
    }
}

export function* removeArticleConceptSaga(action) {
    const {payload: {articleId, concept}} = action;
    try {
        yield put(conceptUpdateStartAC());

        if (action.type === ARTICLE_CONCEPT_REMOVING) {
            yield call(Services.articleService.deleteConcept, articleId, concept.id, false);
        } else if (action.type === ARTICLE_HUMAN_CONCEPT_REMOVING) {
            yield call(Services.articleService.deleteConcept, articleId, concept.id, true);
        }

        const concepts = yield select(getConcepts);
        const updatedConcepts = [];

        for (const articleConcept of concepts) {
            if (articleConcept.id !== concept.id) {
                updatedConcepts.push(articleConcept);
            }
        }
        yield put(articleConceptsUpdatingSuccessAC(updatedConcepts, articleId));

        yield put(conceptUpdateFinishAC());
        yield put(notifySuccessSagaAC('Concept has been removed successfully!'));
    } catch (e) {
        console.error('Error: ', e);
        yield put(notifyDangerSagaAC(`Concepts updating failed: ${e.message}. Try one more time or reload the page!`));
        yield put(conceptUpdateFinishAC());
    }
}

export function* addFoundConceptSaga(action) {
    const {payload: {articleId, concept}} = action;
    try {
        yield put(conceptUpdateStartAC());

        // check concepts if there is already a found concept
        const concepts = yield select(getConcepts);
        yield call(Services.articleService.addConcept, articleId, concept.id);
        concepts.push(concept);
        yield put(articleConceptsUpdatingSuccessAC(concepts, articleId));
        yield put(conceptUpdateFinishAC());
        yield put(notifySuccessSagaAC('Concept has been added successfully!'));
    } catch (e) {
        console.error('Error: ', e);
        yield put(notifyDangerSagaAC(`Concepts updating failed: ${e.message}. Try one more time or reload the page!`));
        yield put(conceptUpdateFinishAC());
    }
}

export function* loadTopConceptChildrenSaga(action) {
    const {payload: {conceptId}} = action;

    try {
        yield put(conceptNodeChildrenLoadingStartAC(conceptId));
        const topConceptsResponce = yield call(Services.articleService.getTaxonomyTopNodes, conceptId);
        const topConcepts = _parseContentFromResponse(topConceptsResponce);

        for (let concept of topConcepts) {
            const conceptsResponse = yield call(Services.articleService.getConceptChildNodes, concept.id);
            concept.hasChildren = conceptsResponse.content.length > 0;
        }

        yield put(conceptNodeChildrenLoadingSuccessAC(conceptId, topConcepts));
    } catch (e) {
        console.error('Error: ', e);
        yield put(notifyDangerSagaAC(`Cannot load tree children: ${e.message}`));
        yield put(conceptNodeChildrenLoadingFailureAC(conceptId));
    }
}

export function* loadConceptChildrenSaga(action) {
    const {payload: {conceptId}} = action;

    try {
        yield put(conceptNodeChildrenLoadingStartAC(conceptId));
        const childConceptsResponce = yield call(Services.articleService.getConceptChildNodes, conceptId);
        const concepts = _parseContentFromResponse(childConceptsResponce);

        // too slow
        for (let concept of concepts) {
            const conceptsResponse = yield call(Services.articleService.getConceptChildNodes, concept.id);
            concept.hasChildren = conceptsResponse.content.length > 0;
        }

        yield put(conceptNodeChildrenLoadingSuccessAC(conceptId, concepts));
    } catch (e) {
        console.error('Error: ', e);
        yield put(notifyDangerSagaAC(`Cannot load tree children: ${e.message}`));
        yield put(conceptNodeChildrenLoadingFailureAC(conceptId));
    }
}

function _isArticleInfoNotFound(article) {
    return !article.id;
}

function _parseContentFromResponse(response = {}) {
    return response.content ? response.content : [];
}

function _parseNextPageOffsetFromResponse(response) {
    if (response.links.next) {
        // This works only in modern browsers Chrome 49+
        // parsedResponse.nextPageOffset = new URL(response.links.next).searchParams.get('offset');
        return Number(Util.parseGetParam('offset', response.links.next));
    }
    return null;
}

export function* watchArticleLoading() {
    yield takeEvery(ARTICLE_LOADING, articleLoadingSaga);
}

export function* watchConceptsSearch() {
    yield takeEvery(CONCEPTS_SEARCH, conceptsSearchSaga);
}

export function* watchConceptsPageLoading() {
    yield takeEvery(CONCEPTS_PAGE_LOADING, conceptsLoadNextPageSaga);
}

export function* watchRemoveArticleConceptSaga() {
    yield takeEvery(ARTICLE_CONCEPT_REMOVING, removeArticleConceptSaga);
}

export function* watchRemoveArticleHumanConceptSaga() {
    yield takeEvery(ARTICLE_HUMAN_CONCEPT_REMOVING, removeArticleConceptSaga);
}

export function* watchAddFoundConceptSaga() {
    yield takeEvery(FOUND_CONCEPT_ADDITION, addFoundConceptSaga);
}

export function* watchLoadTopConceptChildrenSaga() {
    yield takeEvery(LOAD_TOP_CONCEPT_CHILDREN, loadTopConceptChildrenSaga);
}

export function* watchLoadConceptChildrenSaga() {
    yield takeEvery(LOAD_CONCEPT_CHILDREN, loadConceptChildrenSaga);
}


