import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import './styles/searchResultElement.css';
import {Col, Row} from 'react-bootstrap';
import ArticleInlineInfo from '../ArticleInlineInfo';


class SearchResultElement extends PureComponent {

  static propTypes = {
    id: PropTypes.string.isRequired,
    journal: PropTypes.string,
    doi: PropTypes.string.isRequired,
    visibleOnline: PropTypes.bool.isRequired,
    name: PropTypes.string.isRequired,
    publicationDate: PropTypes.string,
    articleNavigate: PropTypes.func.isRequired,
    concepts: PropTypes.array
  };

  constructor(props) {
    super(props);
  }

  handleArticleNavigate = () => {
    this.props.articleNavigate(this.props.id);
  };

  render() {

    return (
      <Row id="cc-result-element-row">
        <Col sm={8}>
          <div id="cc-result-element-body">
            <ArticleInlineInfo
              id={this.props.id}
              journal={this.props.journal}
              doi={this.props.doi}
              visibleOnline={this.props.visibleOnline}
              publicationDate={this.props.publicationDate}
              name={this.props.name}
              concepts={this.props.concepts}
          />
          </div>
        </Col>
        <Col sm={4}>
          <div id="cc-result-element-button">
            <div className="btn btn-sm btn-success" onClick={this.handleArticleNavigate}>
              <span className="glyphicon glyphicon-edit"/> Classify
            </div>
            {/* This is one way to navigate without page reload. But we have to know the page address*/}
            {/*<Link to={'articles/' + this.props.id} className="btn btn-sm btn-success">*/}
            {/*<span className="glyphicon glyphicon-edit"/> Classify*/}
            {/*</Link>*/}
            {/* This leads to page reloading */}
            {/*<a href={this.props.id} className="btn btn-sm btn-success">*/}
            {/*<span className="glyphicon glyphicon-edit"/> Classify*/}
            {/*</a>*/}
          </div>
        </Col>
      </Row>
    );
  }
}

export default SearchResultElement;