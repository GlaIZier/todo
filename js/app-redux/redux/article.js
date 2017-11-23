import {fromJS} from 'immutable';

const initialState = fromJS({
    // Article
    processing: false,

    article: {
        id: '',
        visibleOnline: true,
        name: '',
        doi: '',
        publicationDate: ''
    },

    journal: {
        id: '',
        name: '',
        min: 0,
        max: -1
    },

    taxonomies: [
        // {
        //   id: '',
        //   prefLabel: ''
        // }
    ],

    concepts: [
        // {
        //   id: '',
        //   prefLabel: ''
        // }
    ],

    humanConcepts: [
        // {
        //   id: '',
        //   prefLabel: ''
        // }
    ],

    // tree concepts
    treeConcepts: {},

    // Search concepts
    conceptFilter: '',
    loading: false,
    foundConcepts: [
        // {
        //   id: '',
        //   prefLabel: ''
        // }
    ],

    // null if there are no next results
    nextPageOffset: null,
    nextPageLoading: false,

    // Concept edition
    conceptProcessing: false
});

// reducer
export default function reducer(article = initialState, action = {}) {
    const {type, payload} = action;
    switch (type) {
        case ARTICLE_LOADING_START:
            return article.set('processing', true);

        case ARTICLE_LOADING_FAILURE:
            return article.set('processing', false);

        case ARTICLE_LOADING_SUCCESS:
            return article.set('processing', false).set('article', fromJS(payload.article))
                .set('journal', fromJS(payload.journal)).set('taxonomies', fromJS(payload.taxonomies))
                .set('concepts', fromJS(payload.concepts)).set('humanConcepts', fromJS(payload.humanConcepts));

        case CONCEPTS_SEARCH_START:
            return article.set('loading', true).set('conceptFilter', payload.conceptFilter);

        case CONCEPTS_SEARCH_SUCCESS:
            return article.set('loading', false).set('foundConcepts', fromJS(payload.foundConcepts))
                .set('nextPageOffset', payload.nextPageOffset);

        case CONCEPTS_SEARCH_FAIL:
            return article.set('loading', false);

        case CONCEPTS_PAGE_LOADING_START:
            return article.set('nextPageLoading', true);

        case CONCEPTS_PAGE_LOADING_SUCCESS:
            return article.set('nextPageLoading', false).set('nextPageOffset', payload.nextPageOffset)
                .set('foundConcepts', article.get('foundConcepts').concat(payload.foundConcepts));

        case CONCEPTS_PAGE_LOADING_FAIL:
            return article.set('nextPageLoading', false);

        case CONCEPT_UPDATE_START:
            return article.set('conceptProcessing', true);

        case CONCEPT_UPDATE_FINISH:
            return article.set('conceptProcessing', false);

        case ARTICLE_CONCEPTS_UPDATING_SUCCESS:
            return article.set('concepts', fromJS(payload.concepts));

        case CONCEPT_NODE_CHILDREN_LOADING_START:
            return article.set('treeConcepts', article.get('treeConcepts').set(payload.conceptId,
                {
                    children: fromJS([]),
                    loading: true,
                    loaded: false
                }));

        case FOUND_CONCEPTS_UPDATING_SUCCESS:
            return article.set('foundConcepts', fromJS(payload.foundConcepts));

        case CONCEPT_NODE_CHILDREN_LOADING_SUCCESS:
            return article.set('treeConcepts', article.get('treeConcepts').set(payload.conceptId,
                {
                    children: fromJS(payload.conceptChildren),
                    loading: false,
                    loaded: true
                }));

        case CONCEPT_NODE_CHILDREN_LOADING_FAIL:
            return article.set('treeConcepts', article.get('treeConcepts').set(payload.conceptId,
                {
                    children: fromJS([]),
                    loading: false,
                    loaded: true
                }));

        default:
            return article;
    }
}

// Getters
export const getArticle = state => state.article.get('article') ? state.article.get('article').toJS() : '';
export const getJournal = state => state.article.get('journal') ?
    state.article.get('journal').toJS() : '';
export const getConceptFilter = state => state.article.get('conceptFilter');
export const getNextPageLoading = state => state.article.get('nextPageLoading');
export const getConceptProcessing = state => state.article.get('conceptProcessing');

export const getProcessing = state => state.article.get('processing');
export const getLoading = state => state.article.get('loading');

export const getFoundConcepts = state => state.article.get('foundConcepts').toJS();
export const getNextPageOffset = state => state.article.get('nextPageOffset');

export const getTaxonomies = state => state.article.get('taxonomies').toJS();

export const getConcepts = state => state.article.get('concepts').toJS();
export const getHumanConcepts = state => state.article.get('humanConcepts').toJS();
// this one is a map conceptId -> { children, loaded, loading }, do not turn into JS
export const getTreeConcepts = state => state.article.get('treeConcepts');

// constants
export const ARTICLE_LOADING = 'ARTICLE_LOADING';
export const ARTICLE_LOADING_START = 'ARTICLE_LOADING_START';
export const ARTICLE_LOADING_SUCCESS = 'ARTICLE_LOADING_SUCCESS';
export const ARTICLE_LOADING_FAILURE = 'ARTICLE_LOADING_FAILURE';

export const CONCEPTS_SEARCH = 'CONCEPTS_SEARCH';
export const CONCEPTS_SEARCH_START = 'CONCEPTS_SEARCH_START';
export const CONCEPTS_SEARCH_SUCCESS = 'CONCEPTS_SEARCH_SUCCESS';
export const CONCEPTS_SEARCH_FAIL = 'CONCEPTS_SEARCH_FAIL';

export const CONCEPTS_PAGE_LOADING = 'CONCEPTS_PAGE_LOADING';
export const CONCEPTS_PAGE_LOADING_START = 'CONCEPTS_PAGE_LOADING_START';
export const CONCEPTS_PAGE_LOADING_SUCCESS = 'CONCEPTS_PAGE_LOADING_SUCCESS';
export const CONCEPTS_PAGE_LOADING_FAIL = 'CONCEPTS_PAGE_LOADING_FAIL';

export const ARTICLE_CONCEPT_REMOVING = 'ARTICLE_CONCEPT_REMOVING';
export const ARTICLE_HUMAN_CONCEPT_REMOVING = 'ARTICLE_HUMAN_CONCEPT_REMOVING';
export const FOUND_CONCEPT_ADDITION = 'FOUND_CONCEPT_ADDITION';
export const CONCEPT_UPDATE_START = 'CONCEPT_UPDATE_START';
export const CONCEPT_UPDATE_FINISH = 'CONCEPT_UPDATE_FINISH';
export const ARTICLE_CONCEPTS_UPDATING_SUCCESS = 'ARTICLE_CONCEPTS_UPDATING_SUCCESS';
export const FOUND_CONCEPTS_UPDATING_SUCCESS = 'FOUND_CONCEPTS_UPDATING_SUCCESS';

export const LOAD_TOP_CONCEPT_CHILDREN = 'LOAD_TOP_CONCEPT_CHILDREN';
export const LOAD_CONCEPT_CHILDREN = 'LOAD_CONCEPT_CHILDREN';

export const CONCEPT_NODE_CHILDREN_LOADING_START = 'CONCEPT_NODE_CHILDREN_LOADING_START';
export const CONCEPT_NODE_CHILDREN_LOADING_SUCCESS = 'CONCEPT_NODE_CHILDREN_LOADING_SUCCESS';
export const CONCEPT_NODE_CHILDREN_LOADING_FAIL = 'CONCEPT_NODE_CHILDREN_LOADING_FAIL';

// Action Creators for Reducers
export const articleLoadingStartAC = () => ({
    type: ARTICLE_LOADING_START
});

export const articleLoadingFailureAC = () => ({
    type: ARTICLE_LOADING_FAILURE
});

export const articleLoadingSuccessAC = (article, journal, taxonomies, concepts, humanConcepts) => ({
    type: ARTICLE_LOADING_SUCCESS,
    payload: {article, journal, taxonomies, concepts, humanConcepts}
});

export const conceptsSearchStartAC = conceptFilter => ({
    type: CONCEPTS_SEARCH_START,
    payload: {conceptFilter}
});

export const conceptsSearchSuccessAC = (foundConcepts, nextPageOffset) => ({
    type: CONCEPTS_SEARCH_SUCCESS,
    payload: {foundConcepts, nextPageOffset}
});

export const conceptsSearchFailAC = () => ({
    type: CONCEPTS_SEARCH_FAIL
});

export const conceptsPageLoadingStartAC = () => ({
    type: CONCEPTS_PAGE_LOADING_START
});

export const conceptsPageLoadingSuccessAC = (foundConcepts, nextPageOffset) => ({
    type: CONCEPTS_PAGE_LOADING_SUCCESS,
    payload: {foundConcepts, nextPageOffset}
});

export const conceptsPageLoadingFailAC = () => ({
    type: CONCEPTS_PAGE_LOADING_FAIL
});

export const conceptUpdateStartAC = () => ({
    type: CONCEPT_UPDATE_START
});

export const conceptUpdateFinishAC = () => ({
    type: CONCEPT_UPDATE_FINISH
});

export const articleConceptsUpdatingSuccessAC = (concepts, articleId) => ({
    type: ARTICLE_CONCEPTS_UPDATING_SUCCESS,
    payload: {concepts, articleId}
});

export const foundConceptsUpdatingSuccessAC = foundConcepts => ({
    type: FOUND_CONCEPTS_UPDATING_SUCCESS,
    payload: {foundConcepts}
});

// Action Creators for Sagas
export const articleLoadingSagaAC = (id) => ({
    type: ARTICLE_LOADING,
    payload: {id}
});

export const conceptsSearchSagaAC = (conceptFilter) => ({
    type: CONCEPTS_SEARCH,
    payload: {conceptFilter}
});

export const conceptsPageLoadingSagaAC = () => ({
    type: CONCEPTS_PAGE_LOADING
});

export const articleConceptRemovingSagaAC = (articleId, concept) => ({
    type: ARTICLE_CONCEPT_REMOVING,
    payload: {articleId, concept}
});

export const articleHumanConceptRemovingSagaAC = (articleId, concept) => ({
    type: ARTICLE_HUMAN_CONCEPT_REMOVING,
    payload: {articleId, concept}
});

export const foundConceptAdditionSagaAC = (articleId, concept) => ({
    type: FOUND_CONCEPT_ADDITION,
    payload: {articleId, concept}
});

export const loadTopConceptChildrenSagaAC = (conceptId) => ({
    type: LOAD_TOP_CONCEPT_CHILDREN,
    payload: {conceptId}
});

export const loadConceptChildrenSagaAC = (conceptId) => ({
    type: LOAD_CONCEPT_CHILDREN,
    payload: {conceptId}
});

export const conceptNodeChildrenLoadingStartAC = (conceptId) => ({
    type: CONCEPT_NODE_CHILDREN_LOADING_START,
    payload: {conceptId}
});

export const conceptNodeChildrenLoadingSuccessAC = (conceptId, conceptChildren) => ({
    type: CONCEPT_NODE_CHILDREN_LOADING_SUCCESS,
    payload: {conceptId, conceptChildren}
});

export const conceptNodeChildrenLoadingFailureAC = (conceptId) => ({
    type: CONCEPT_NODE_CHILDREN_LOADING_FAIL,
    payload: {conceptId}
});
