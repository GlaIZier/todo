import {fromJS} from 'immutable';
import {ARTICLE_CONCEPTS_UPDATING_SUCCESS} from './article';

const initialState = fromJS({
  articles: [
    // {
    //   'id': '2febe14f-7e18-301d-b7e7-4b530dcccbec',
    //   'visibleOnline': true,
    //   'name': 'Title25',
    //   'doi': 'doi25',
    //   'publicationDate': '2015-12-15',
    //   'journal': 'journal25'
    // }
  ],
  // null if there are no next results
  nextPageOffset: null,
  loading: false,
  errorMessage: ''
});

// reducer
export default function reducer(articles = initialState, action = {}) {
  const {type, payload} = action;
  switch (type) {
    case ARTICLES_SEARCH_START:
      return articles.set('loading', true).set('articles', fromJS([])).set('nextPageOffset', null);

    case ARTICLES_PAGE_LOADING_START:
      return articles.set('loading', true);

    case ARTICLES_SEARCH_SUCCESS:
      return articles.set('nextPageOffset', payload.articles.nextPageOffset).set('articles',
          fromJS(payload.articles.articles)).set('loading', false);

    case ARTICLES_PAGE_LOADING_SUCCESS:
      return articles.set('nextPageOffset', payload.articles.nextPageOffset).set('articles',
          articles.get('articles').concat(fromJS(payload.articles.articles))).set('loading', false);

    case ARTICLES_PAGE_LOADING_FAIL:
      return articles.set('loading', false).set('errorMessage', payload.errorMessage);

    case ARTICLE_CONCEPTS_UPDATING_SUCCESS: {


        let articles_copy = articles.get('articles').slice().map(function (article) {
            if (article.get('id') === payload.articleId) {

                return article.merge({'concepts': payload.concepts});
            }
            return article;
        });
        return articles.set('articles', articles_copy);

    }

    default:
      return articles;
  }
}

// Getters
export const getArticles = state => state.articles.get('articles').toJS();
export const getNextPageOffset = state => state.articles.get('nextPageOffset');
export const getLoading = state => state.articles.get('loading');

// constants
export const ARTICLES_PAGE_LOADING = 'ARTICLES_PAGE_LOADING';
export const ARTICLES_SEARCH_START = 'ARTICLES_SEARCH_START';
export const ARTICLES_PAGE_LOADING_START = 'ARTICLES_PAGE_LOADING_START';
export const ARTICLES_SEARCH_SUCCESS = 'ARTICLES_SEARCH_SUCCESS';
export const ARTICLES_PAGE_LOADING_SUCCESS = 'ARTICLES_PAGE_LOADING_SUCCESS';
export const ARTICLES_PAGE_LOADING_FAIL = 'ARTICLES_PAGE_LOADING_FAIL';

// Action Creators for Reducers
export const articlesSearchStartAC = () => ({type: ARTICLES_SEARCH_START});
export const articlesPageLoadingStartAC = () => ({type: ARTICLES_PAGE_LOADING_START});
export const articlesSearchSuccessAC = articles => ({type: ARTICLES_SEARCH_SUCCESS, payload: {articles}});
export const articlesPageLoadingSuccessAC = articles => ({type: ARTICLES_PAGE_LOADING_SUCCESS, payload: {articles}});
export const articlesPageLoadingFailAC = (errorMessage) => ({
  type: ARTICLES_PAGE_LOADING_FAIL,
  payload: {errorMessage}
});

// Action Creators for Sagas
export const loadNextPageSagaAC = () => ({
  type: ARTICLES_PAGE_LOADING
});