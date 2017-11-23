import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import './styles/articleInfo.css';

class ArticleInfo extends PureComponent {

  static propTypes = {
    id: PropTypes.string.isRequired,
    journal: PropTypes.object,
    doi: PropTypes.string.isRequired,
    visibleOnline: PropTypes.bool.isRequired,
    name: PropTypes.string.isRequired,
    publicationDate: PropTypes.string,
    concepts: PropTypes.array
  };

  constructor(props) {
    super(props);
  }

  render() {

    const journalInfo =
      (this.props.journal && this.props.journal !== '') ?
        (<div><b>Journal: </b> {this.props.journal.name}</div>)
        :
        '';

    const classifiedFeedback =
      (this.props.journal && this.props.journal !== '' && this.props.journal.min > 0
          && this.props.journal.max > this.props.journal.min && this.props.concepts) ?
          (<div>
              <b>Classification completeness: </b> {(this.props.concepts.length * 100 ) / this.props.journal.min} %
          </div>)
          :
          '';

    return (
      <div>
          <div><h4>{this.props.name}</h4>

        </div>
          <div><b>ID: </b> <i>{this.props.id}</i></div>
        {journalInfo}
        <div><b>DOI: </b><i>{this.props.doi}</i></div>
        <div><b>Status: </b><i>{this.props.visibleOnline ? 'Published' : 'Unpublished'}</i></div>
        <div><b>Publication date: </b><i>{this.props.publicationDate ? this.props.publicationDate : '-'}</i></div>
        {classifiedFeedback}
      </div>
    );
  }
}

export default ArticleInfo;