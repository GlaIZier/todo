import React, {PureComponent} from 'react';
// import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {searchSagaAC} from '../../redux/search';
import {loadNextPageSagaAC} from '../../redux/articles';
import {articleNavigateSagaAC} from '../../redux/navigate';
// import SearchPanel from '../../components/SearchPanel';
// import SearchResult from '../../components/SearchResult';
// import InfiniteScroll from '../../components/InfiniteScroll';
import './styles/tasks.css';

class Tasks extends PureComponent {

  // static propTypes = {
  //     filters: PropTypes.object.isRequired,
  //     processing: PropTypes.bool.isRequired,
  //     searchSagaAC: PropTypes.func.isRequired,
  //     articles: PropTypes.array.isRequired,
  //     nextPageOffset: PropTypes.number,
  //     loadNextPageSagaAC: PropTypes.func.isRequired,
  //     loading: PropTypes.bool.isRequired,
  //     articleNavigateSagaAC: PropTypes.func.isRequired
  // };

  render() {
    return (
      <h3>Hello from tasks!</h3>
    )
    // return (
    //     <div>
    //         <SearchPanel
    //             filters={this.props.filters}
    //             processing={this.props.processing}
    //             search={this.props.searchSagaAC}
    //         />
    //         <div className="container">
    //             <InfiniteScroll
    //                 loadNextPage={this.props.loadNextPageSagaAC}
    //                 hasMore={!!(this.props.nextPageOffset && this.props.nextPageOffset > 0)}
    //                 globally={false}
    //                 loading={this.props.loading}
    //             >
    //                 <div id='cc-articles-search-results'>
    //                     <SearchResult
    //                         articles={this.props.articles}
    //                         articleNavigate={this.props.articleNavigateSagaAC}
    //                     />
    //                 </div>
    //             </InfiniteScroll>
    //         </div>
    //     </div>
    // )
  }
}

function mapStateToProps(state) {
  console.log(state);
  return {
    // filters: getFilters(state),
    // processing: getProcessing(state),
    // articles: getTasks(state),
    // nextPageOffset: getNextPageOffset(state),
    // loading: getLoading(state)
  }
}

export default connect(mapStateToProps, {searchSagaAC, loadNextPageSagaAC, articleNavigateSagaAC})(Tasks)