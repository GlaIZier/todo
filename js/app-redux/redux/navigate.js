export const ROUTE_NAVIGATE = 'ROUTE_NAVIGATE';
export const ARTICLE_NAVIGATE = 'ARTICLE_NAVIGATE';

export const navigateSagaAC = (path, props) => ({type: ROUTE_NAVIGATE, payload: {path, props}});
export const articleNavigateSagaAC = (id) => ({type: ARTICLE_NAVIGATE, payload: {id}});
