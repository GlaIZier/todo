const appConfig = () => {
  // const baseUrl = '/api/v1';
  const apiBaseUrl = 'https://localhost:8443/todo/api/';

  const loginApiUrl = apiBaseUrl + 'auth/login';
  const logoutApiUrl = apiBaseUrl + 'auth/me/logout';

  let journalsSearchBaseUrl = apiBaseUrl + '';
  let articlesSearchBaseUrl = apiBaseUrl + '';

  let articleInfoBaseUrlTemplate = apiBaseUrl + '/ArticleProducts/<%-props.id%>';
  let articleJournalBaseUrlTemplate = apiBaseUrl + '';
  let articleConceptsBaseUrlTemplate = apiBaseUrl + '';
  let articleHumanConceptsBaseUrlTemplate = apiBaseUrl + '';
  // here lot's of empty results for journals
  let journalPolicyBaseUrlTemplate = loginApiUrl + '';

  let journalTaxonomiesBaseUrlTemplate = apiBaseUrl + '';
  let topConceptsBaseUrlTemplate = apiBaseUrl + '';
  let childConceptsBaseUrlTemplate = apiBaseUrl + '';

  let conceptsSearchBaseUrl = apiBaseUrl + '';
  let conceptEditionBaseUrlTemplate = apiBaseUrl + '';
  let conceptHumanEditionBaseUrlTemplate = apiBaseUrl + '';

  // Enable mock
  // if (__LOCAL__) {
  //   journalsSearchBaseUrl = '';
  //   articlesSearchBaseUrl = '';
  //
  //   articleInfoBaseUrlTemplate = '';
  //   articleJournalBaseUrlTemplate = '';
  //   articleConceptsBaseUrlTemplate = '';
  //   articleHumanConceptsBaseUrlTemplate = '';
  //   // here lot's of empty results for journals
  //   journalPolicyBaseUrlTemplate = '';
  //
  //   journalTaxonomiesBaseUrlTemplate = '';
  //   topConceptsBaseUrlTemplate = '';
  //   childConceptsBaseUrlTemplate = '';
  //
  //   conceptsSearchBaseUrl = '';
  //   conceptEditionBaseUrlTemplate = '';
  //   conceptHumanEditionBaseUrlTemplate = '';
  // }


  const statusAll = 'all';
  const statusPublished = 'published';
  const statusUnpublished = 'unpublished';
  const dateSort = 'calc';
  const nameSort = 'name';

  // notifications
  const notificationsTimeoutMs = 3000;

  // token cookie
  const apiTokenExpireDays = 1;
  const apiTokenCookieName = 'todo-api-token-cookie';

  // localStorage
  const localStorageUserItemName = 'todo-user';

  return {
    // baseUrl: baseUrl,
    loginApiUrl: loginApiUrl,
    logoutApiUrl: logoutApiUrl,

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

      notificationTimeout: notificationsTimeoutMs,

      apiTokenExpireDays: apiTokenExpireDays,
      apiTokenCookieName: apiTokenCookieName,

      localStorageUserItemName: localStorageUserItemName
    }
  }
};

export default appConfig();
