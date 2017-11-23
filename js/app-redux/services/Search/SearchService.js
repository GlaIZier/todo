import $ from 'jquery';
import Q from 'q';
// import Promise from 'promise';

class SearchService {

  static getSearchBodyTemplate = () => {
      return {
          query: {
              operator: 'and',
              operands: []
          }
      };
  };

  static getCalcSearchBodyTemplate = () => {
    return {
      forceIndex: 'ElasticSearch',
      query: {
        operator: 'and',
        operands: [
            {
                field:'calc',
                operator:'gte',
                value:'2017-06-01'
            }]
      },
      calculated: {
        fields: [
            {
                field: 'calc',
                operator: 'if',
                data: {
                    condition : 'visibleOnline',
                    field1 : 'publicationDate',
                    field2 : 'productionStartedAtTime'
                }
            }
        ]
      }
    };
  };

  static getNameMatchQueryTemplate = () => {
    return {
      field: 'name',
      operator: 'starts',
      'value': ''
    };
  };

  static getDoiMatchQueryTemplate = () => {
    return {
      field: 'name',
      operator: 'starts',
      'value': ''
    };
  };

  constructor(config) {
    this.config = config;
  }

  searchJournals = (journal = '') => {
    const url = this.config.journalsSearchBaseUrl;
    const body = this._generateJournalSearchRequestBody(journal);
    const defer = Q.defer();

    $.ajax({
      url,
      method: 'POST',
      contentType: 'application/json',
      dataType: 'json',
      data: JSON.stringify(body)
    })
      .then(payload => {
        defer.resolve(payload)
      })
      .fail(e => defer.reject(e));
    return defer.promise;
  };

  _generateJournalSearchRequestBody = (journal = '') => {
    let body = SearchService.getSearchBodyTemplate();
    let nameQuery = SearchService.getNameMatchQueryTemplate();
    nameQuery.value = journal;
    body.query.operands.push(nameQuery);
    return body;
  };

  searchArticles = (journalIds = [], filters = {}, offset = null) => {
    const url = this._generateArticlesSearchUrl(this.config.articlesSearchBaseUrl, filters.sort, offset);
    const body = this._generateArticlesSearchRequestBody(journalIds, filters);
    const defer = Q.defer();

    $.ajax({
      url,
      method: 'POST',
      contentType: 'application/json',
      dataType: 'json',
      data: JSON.stringify(body)
    })
      .then(payload => {
        defer.resolve(payload)
      })
      .fail(e => defer.reject(e));
    return defer.promise;
  };

  _generateArticlesSearchUrl = (baseUrl = '', sort = null, offset = null) => {
    let url = `${baseUrl}&`;

    if (sort === this.config.constants.dateSort)
      url = `${url}order_by=${sort}:desc&`;
    else if (sort === this.config.constants.nameSort)
      url = `${url}order_by=${sort}&`;

    return offset ? `${url}offset=${offset}` : url;
  };

  _generateArticlesSearchRequestBody = (journalIds = [], filters = {}) => {
    let body = SearchService.getCalcSearchBodyTemplate();

    if (journalIds.length > 0) {
      let journal = {};
      journal.relation = 'isPartOfJournal';
      journal.operator = 'in';
      journal.values = journalIds;
      body.query.operands.push(journal);
    }

    if (filters.doi && filters.doi.length > 0) {
      let doi = SearchService.getDoiMatchQueryTemplate();
      doi.field = 'doi';
      doi.value = filters.doi;
      body.query.operands.push(doi);
    }

    if (filters.name && filters.name.length > 0) {
      let name = SearchService.getNameMatchQueryTemplate();
      name.value = filters.name;
      body.query.operands.push(name);
    }

    if (filters.status !== this.config.constants.statusAll) {
      let visibleOnline = SearchService.getNameMatchQueryTemplate();
      visibleOnline.field = 'visibleOnline';
      visibleOnline.operator = 'eq';
      visibleOnline.value = filters.status === this.config.constants.statusPublished;
      body.query.operands.push(visibleOnline);
    }

    return body;
  };
  
}

export default SearchService;