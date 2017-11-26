const appConfig = () => {
  // const baseUrl = '/api/v1';
  const apiBaseUrl = __LOCAL__ ? '' : '';
  const authApiBaseUrl = __LOCAL__ ? '' : '';

  let journalsSearchBaseUrl = apiBaseUrl + '';
  let articlesSearchBaseUrl = apiBaseUrl + '';

  let articleInfoBaseUrlTemplate = apiBaseUrl + '/ArticleProducts/<%-props.id%>';
  let articleJournalBaseUrlTemplate = apiBaseUrl + '';
  let articleConceptsBaseUrlTemplate = apiBaseUrl + '';
  let articleHumanConceptsBaseUrlTemplate = apiBaseUrl + '';
  // here lot's of empty results for journals
  let journalPolicyBaseUrlTemplate = authApiBaseUrl + '';

  let journalTaxonomiesBaseUrlTemplate = apiBaseUrl + '';
  let topConceptsBaseUrlTemplate = apiBaseUrl + '';
  let childConceptsBaseUrlTemplate = apiBaseUrl + '';

  let conceptsSearchBaseUrl = apiBaseUrl + '';
  let conceptEditionBaseUrlTemplate = apiBaseUrl + '';
  let conceptHumanEditionBaseUrlTemplate = apiBaseUrl + '';

  // Enable mock
  if (__LOCAL__) {
    journalsSearchBaseUrl = '';
    articlesSearchBaseUrl = '';

    articleInfoBaseUrlTemplate = '';
    articleJournalBaseUrlTemplate = '';
    articleConceptsBaseUrlTemplate = '';
    articleHumanConceptsBaseUrlTemplate = '';
    // here lot's of empty results for journals
    journalPolicyBaseUrlTemplate = '';

    journalTaxonomiesBaseUrlTemplate = '';
    topConceptsBaseUrlTemplate = '';
    childConceptsBaseUrlTemplate = '';

    conceptsSearchBaseUrl = '';
    conceptEditionBaseUrlTemplate = '';
    conceptHumanEditionBaseUrlTemplate = '';
  }


  const statusAll = 'all';
  const statusPublished = 'published';
  const statusUnpublished = 'unpublished';
  const dateSort = 'calc';
  const nameSort = 'name';

  return {
    // baseUrl: baseUrl,
    authApiBaseUrl: authApiBaseUrl,

    journalsSearchBaseUrl: journalsSearchBaseUrl,
    articlesSearchBaseUrl: articlesSearchBaseUrl,

    articleInfoBaseUrlTemplate: articleInfoBaseUrlTemplate,
    articleJournalBaseUrlTemplate: articleJournalBaseUrlTemplate,
    articleConceptsBaseUrlTemplate: articleConceptsBaseUrlTemplate,
    articleHumanConceptsBaseUrlTemplate: articleHumanConceptsBaseUrlTemplate,
    journalTaxonomiesBaseUrlTemplate: journalTaxonomiesBaseUrlTemplate,
    topConceptsBaseUrlTemplate: topConceptsBaseUrlTemplate,
    childConceptsBaseUrlTemplate: childConceptsBaseUrlTemplate,
    journalPolicyBaseUrlTemplate: journalPolicyBaseUrlTemplate,
    conceptsSearchBaseUrl: conceptsSearchBaseUrl,
    conceptEditionBaseUrlTemplate: conceptEditionBaseUrlTemplate,
    conceptHumanEditionBaseUrlTemplate: conceptHumanEditionBaseUrlTemplate,
    // add here another settings if needed

    constants: {
      statusAll: statusAll,
      statusPublished: statusPublished,
      statusUnpublished: statusUnpublished,
      dateSort: dateSort,
      nameSort: nameSort,
      notificationTimeout: 3000
    }
  }
};

export default appConfig();
