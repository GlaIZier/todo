const appConfig = () => {
  // const baseUrl = '/api/v1';
  const apiBaseUrl = 'https://localhost:8443/todo/api/';

  const loginApiUrl = apiBaseUrl + 'auth/login';
  const logoutApiUrl = apiBaseUrl + 'auth/me/logout';

  const registerApiUrl = apiBaseUrl + 'users';

  const tasksApiUrl = apiBaseUrl + 'me/tasks';

  // let articleInfoBaseUrlTemplate = apiBaseUrl + '/ArticleProducts/<%-props.id%>';

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
  const apiTokenHeaderName = 'TODO-API-TOKEN-HEADER';

  // localStorage
  const localStorageUserItemName = 'todo-user';

  return {
    // baseUrl: baseUrl,
    loginApiUrl: loginApiUrl,
    logoutApiUrl: logoutApiUrl,

    registerApiUrl: registerApiUrl,

    tasksApiUrl: tasksApiUrl,

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
      apiTokenHeaderName: apiTokenHeaderName,

      localStorageUserItemName: localStorageUserItemName
    }
  }
};

export default appConfig();
