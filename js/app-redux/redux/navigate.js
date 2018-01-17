export const ROUTE_NAVIGATE = 'ROUTE_NAVIGATE';

export const navigateSagaAC = (path, props) => ({type: ROUTE_NAVIGATE, payload: {path, props}});