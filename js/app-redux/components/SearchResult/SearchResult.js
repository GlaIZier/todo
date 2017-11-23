import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import './styles/searchResult.css';
import {Col, Panel, Row} from 'react-bootstrap';
import SearchResultElement from '../SearchResultElement';


class SearchResult extends PureComponent {

  static propTypes = {
    articles: PropTypes.array.isRequired,
    articleNavigate: PropTypes.func.isRequired
  };

  constructor(props) {
    super(props);
  }


  render() {

    const resultPanelTitle = (
      <h3>
        Results
        {/*<div className="panel-heading">*/}
        {/*<h3 className="panel-title cc-display-inline">Results</h3>*/}
        {/*<div id="cc-result-panel-title-sort" className="panel-title">*/}
        {/*<p id="cc-result-panel-title-sort-heading" className="cc-display-inline"> Sort by </p>*/}
        {/*<FormGroup className="cc-display-inline">*/}
        {/*<Radio name="radioGroup" inline defaultChecked={true}>*/}
        {/*Date*/}
        {/*</Radio>*/}
        {/*<Radio name="radioGroup" inline>*/}
        {/*Title*/}
        {/*</Radio>*/}
        {/*</FormGroup>*/}
        {/*</div>*/}
        {/*</div>*/}
      </h3>
    );

    const that = this;

    const searchResult = (
      <Panel id='cc-result-panel-body' header={resultPanelTitle}>
        <Row>
          <Col sm={12}>
            {this.props.articles.map(function (article, i) {
              return <SearchResultElement
                id={article.id}
                journal={article.journal}
                doi={article.doi}
                visibleOnline={article.visibleOnline}
                name={article.name}
                publicationDate={article.publicationDate}
                articleNavigate={that.props.articleNavigate}
                concepts={article.concepts}
                key={i}
              />
            })}
          </Col>
        </Row>
      </Panel>
    );

    return (
      <div id="search-result">
        {(this.props.articles.length > 0) ? searchResult : ''}
      </div>
    );
  }

}

export default SearchResult;