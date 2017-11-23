import $ from 'jquery';
import Q from 'q';
import {stringify} from 'query-string';
import _ from 'underscore';

class ArticleService {

  static getSearchBodyTemplate = () => {
    return {
      query: {
        operator: 'and',
        operands: []
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


  constructor(config) {
    this.config = config;
  }

  getArticleInfo = (id = '') => {
    const props = {
      id: id
    };
    return this._getForUrl(this.config.articleInfoBaseUrlTemplate, props);
  };

  getArticleJournal = (id = '') => {
    const props = {
      id: id
    };
    return this._getForUrl(this.config.articleJournalBaseUrlTemplate, props);
  };

  getJournalPolicy = (id = '') => {
      const props = {
          id: id
      };
      return this._getForUrl(this.config.journalPolicyBaseUrlTemplate, props);
  };

  getJournalTaxonomies = (id = '') => {
    const props = {
      id: id
    };
    return this._getForUrl(this.config.journalTaxonomiesBaseUrlTemplate, props);
  };

  getTaxonomyTopNodes = (id = '') => {
      const props = {
          id: id
      };
      return this._getForUrl(this.config.topConceptsBaseUrlTemplate, props);
  };

  getConceptChildNodes = (id = '') => {
      const props = {
          id: id
      };
      return this._getForUrl(this.config.childConceptsBaseUrlTemplate, props);
  };

  getArticleConcepts = (id = '') => {
    const props = {
      id: id
    };
    return this._getForUrl(this.config.articleConceptsBaseUrlTemplate, props);
  };

  getArticleHumanConcepts = (id = '') => {
      const props = {
          id: id
      };
      return this._getForUrl(this.config.articleHumanConceptsBaseUrlTemplate, props);
  };

  searchConcepts = (taxonomies = [], conceptFilter = '', offset = null) => {
    const url = offset ? `${this.config.conceptsSearchBaseUrl}&offset=${offset}` : this.config.conceptsSearchBaseUrl;
    const body = this._generateConceptsSearchRequestBody(taxonomies, conceptFilter);
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

  deleteConcept = (articleId = '', conceptId = '', human) => {
    return this._editConcept('DELETE', articleId, conceptId, human);
  };

  addConcept = (articleId = '', conceptId = '') => {
    return this._editConcept('PUT', articleId, conceptId, true);
  };

  _getForUrl = (urlTemplate = '', props = null) => {
    let url;
    if (props) {
      url = this._generateUrlFromTemplate(urlTemplate, props);
    } else {
      url = urlTemplate;
    }
    const defer = Q.defer();

    $.ajax({
      url,
      headers: {
         'Cache-Control': 'no-cache'
      },
      method: 'GET'
    })
      .then(payload => {
        defer.resolve(payload)
      })
      .fail(e => defer.reject(e));

    return defer.promise;
  };

  _editConcept = (method = 'PUT', articleId = '', conceptId = '', human = false) => {
    const props = {
      articleId: articleId,
      conceptId: conceptId
    };
    const url = human ?
        this._generateUrlFromTemplate(this.config.conceptHumanEditionBaseUrlTemplate, props)
        : this._generateUrlFromTemplate(this.config.conceptEditionBaseUrlTemplate, props);
    const defer = Q.defer();

    $.ajax({
      url,
      method: method
    })
      .then(payload => {
        defer.resolve(payload)
      })
      .fail(e => defer.reject(e));
    return defer.promise;
  };

  _generateUrlFromTemplate = (url = '', props = {}) => {
    const template = _.template(url);
    const encodedProps = Object.assign({}, props);
    for (const prop in encodedProps) {
      if (encodedProps.hasOwnProperty(prop)) {
        if (typeof encodedProps[prop] === 'object') {
          encodedProps[prop] = stringify(encodedProps[prop], true);
        } else {
          encodedProps[prop] = encodeURIComponent(encodedProps[prop]);
        }
      }
    }
    return template({props: encodedProps});
  };

  _generateConceptsSearchRequestBody = (taxonomies = [], conceptFilter = '') => {
    let body = ArticleService.getSearchBodyTemplate();

    if (taxonomies.length > 0) {
      let taxonomyQuery = {};
      taxonomyQuery.relation = 'inScheme';
      taxonomyQuery.operator = 'in';
      taxonomyQuery.values = [];
      for (const tax of taxonomies) {
        taxonomyQuery.values.push(tax.id);
      }
      body.query.operands.push(taxonomyQuery);
    }

    if (conceptFilter && conceptFilter.length > 0) {
      let conceptFilterQuery = ArticleService.getNameMatchQueryTemplate();
      conceptFilterQuery.field = 'prefLabel';
      conceptFilterQuery.value = conceptFilter;
      body.query.operands.push(conceptFilterQuery);
    }

    return body;
  };

}

export default ArticleService;