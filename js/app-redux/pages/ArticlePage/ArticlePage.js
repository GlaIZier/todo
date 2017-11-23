import React, {PureComponent} from 'react';
import {Article} from '../../containers/Article';


class ArticlePage extends PureComponent {
  render() {
    return (
      <Article
        articleId={this.props.params.articleId}
      />
    );
  }
}

export default ArticlePage;
