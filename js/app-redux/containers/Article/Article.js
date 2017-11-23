import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {Col, Panel, Tab, Tabs} from 'react-bootstrap';
import {connect} from 'react-redux';
import {
    articleConceptRemovingSagaAC,
    articleHumanConceptRemovingSagaAC,
    articleLoadingSagaAC,
    conceptsPageLoadingSagaAC,
    conceptsPageLoadingStartAC,
    conceptsSearchSagaAC,
    foundConceptAdditionSagaAC,
    getArticle,
    getConceptFilter,
    getConceptProcessing,
    getConcepts,
    getFoundConcepts,
    getHumanConcepts,
    getJournal,
    getLoading,
    getNextPageLoading,
    getNextPageOffset,
    getProcessing,
    getTaxonomies,
    getTreeConcepts,
    loadConceptChildrenSagaAC,
    loadTopConceptChildrenSagaAC
} from '../../redux/article';
import ArticleInfo from '../../components/ArticleInfo';
import ConceptsList from '../../components/ConceptsList';
import TextSearch from '../../components/TextSearch';
import InfiniteScroll from '../../components/InfiniteScroll';
import LoadingGif from '../../images/loader.gif';
import './styles/article.css';
import ConceptsBrowse from '../../components/ConceptsBrowse/ConceptsBrowse';

class Article extends PureComponent {

    static propTypes = {
        articleId: PropTypes.string.isRequired,
        article: PropTypes.object.isRequired,

        concepts: PropTypes.array.isRequired,
        humanConcepts: PropTypes.array.isRequired,

        conceptFilter: PropTypes.string.isRequired,
        foundConcepts: PropTypes.array,

        processing: PropTypes.bool.isRequired,
        conceptProcessing: PropTypes.bool,
        loading: PropTypes.bool.isRequired,

        nextPageOffset: PropTypes.number,
        nextPageLoading: PropTypes.bool.isRequired,

        journal: PropTypes.object.isRequired,
        taxonomies: PropTypes.array.isRequired,

        treeConcepts: PropTypes.object,
        topConcepts: PropTypes.array,
    };

    componentDidMount() {
        this.props.articleLoadingSagaAC(this.props.articleId);
    }

    render() {
        const loadingGif = (
            <div id="cc-loading-gif">
                <img src={LoadingGif}/>
            </div>);

        const articlePanelHeader = (
            <h3>Article</h3>
        );

        const conceptsPanelHeader = (
            <h3>Article's concepts</h3>
        );

        const searchResultPanelHeader = (
            <h3>Search results</h3>
        );

        const noSearchResultPanelHeader = (
            <h3>No concepts to display</h3>
        );

        const noTaxonomiesPanelHeader = (
            <h3>No taxonomies assigned to this journal</h3>
        );

        const noClassificationsPanelHeader = (
            <h3>The article has no concepts</h3>
        );

        const browser = (
            <div style={{paddingTop: '10px'}}>
                {this.props.taxonomies.length > 0
                    ?
                    (
                        <ConceptsBrowse
                            articleId={this.props.articleId}
                            taxonomies={this.props.taxonomies}
                            concepts={this.props.concepts}
                            humanConcepts={this.props.humanConcepts}
                            conceptProcessing={this.props.conceptProcessing}

                            treeConcepts={this.props.treeConcepts}
                            loadTopChildren={this.props.loadTopConceptChildrenSagaAC}
                            loadChildren={this.props.loadConceptChildrenSagaAC}

                            nodeButtonStyle='glyphicon-chevron-right'
                            nodeOpenedButtonStyle='glyphicon-chevron-down'

                            addButtonStyle='glyphicon-plus'
                            deleteButtonStyle='glyphicon-trash'

                            addConcept={this.props.foundConceptAdditionSagaAC}
                            addHumanConcept={this.props.foundConceptAdditionSagaAC}
                            deleteConcept={this.props.articleConceptRemovingSagaAC}
                            deleteHumanConcept={this.props.articleHumanConceptRemovingSagaAC}
                        />
                    )
                    : (<div>
                        {noTaxonomiesPanelHeader}
                    </div>)
                }
            </div>
        );

        const foundConcepts = (
            <div>
                {this.props.foundConcepts.length > 0
                    ?
                    (
                        <div>
                            {searchResultPanelHeader}
                            <InfiniteScroll
                                loadNextPage={this.props.conceptsPageLoadingSagaAC}
                                hasMore={!!(this.props.nextPageOffset && this.props.nextPageOffset > 0)}
                                loading={this.props.nextPageLoading}
                                globally={false}
                                startLoadNextPage={this.props.conceptsPageLoadingStartAC}
                            >
                                <div id='cc-found-concepts-list-group'>
                                    <ConceptsList
                                        articleId={this.props.article.id}

                                        concepts={this.props.foundConcepts}
                                        articleConcepts={this.props.concepts}
                                        humanConcepts={this.props.humanConcepts}

                                        addButtonStyle='glyphicon-plus'
                                        deleteButtonStyle='glyphicon-trash'

                                        addConcept={this.props.foundConceptAdditionSagaAC}
                                        addHumanConcept={this.props.foundConceptAdditionSagaAC}

                                        deleteConcept={this.props.articleConceptRemovingSagaAC}
                                        deleteHumanConcept={this.props.articleHumanConceptRemovingSagaAC}

                                        conceptProcessing={this.props.conceptProcessing}
                                    />
                                </div>
                            </InfiniteScroll>
                        </div>
                    )
                    : (<div>
                        {noSearchResultPanelHeader}
                    </div>)

                }
            </div>
        );

        const foundConceptsContainer = (
            <div>
                {this.props.loading ?
                    loadingGif
                    :
                    foundConcepts
                }
            </div>
        );

        const articleContainer = (
            <div className="scrollable-content">
                <div className="container">
                    <Col md={8}>
                        <Tabs defaultActiveKey={2} id="tabs">
                            <Tab eventKey={1} title="Search">
                                <TextSearch
                                    placeholder='Search for a concept'
                                    value={this.props.conceptFilter}
                                    loading={this.props.loading}
                                    search={this.props.conceptsSearchSagaAC}
                                />
                                {foundConceptsContainer}
                            </Tab>
                            <Tab eventKey={2} title="Browse">
                                {browser}
                            </Tab>
                        </Tabs>
                    </Col>
                    <Col md={4}>
                        <Panel header={articlePanelHeader}>
                            <ArticleInfo
                                id={this.props.article.id}
                                journal={this.props.journal}
                                doi={this.props.article.doi}
                                visibleOnline={this.props.article.visibleOnline}
                                publicationDate={this.props.article.publicationDate}
                                name={this.props.article.name}
                                concepts={this.props.concepts}
                            />
                        </Panel>
                        {this.props.concepts.length > 0
                            ?
                            (<Panel header={conceptsPanelHeader}>
                                <div id='cc-article-concepts-list-group'>

                                    <ConceptsList
                                        articleId={this.props.article.id}

                                        concepts={this.props.concepts}
                                        articleConcepts={this.props.concepts}
                                        humanConcepts={this.props.humanConcepts}

                                        addButtonStyle='glyphicon-plus'
                                        deleteButtonStyle='glyphicon-trash'

                                        addConcept={this.props.foundConceptAdditionSagaAC}
                                        addHumanConcept={this.props.foundConceptAdditionSagaAC}

                                        deleteConcept={this.props.articleConceptRemovingSagaAC}
                                        deleteHumanConcept={this.props.articleHumanConceptRemovingSagaAC}

                                        conceptProcessing={this.props.conceptProcessing}
                                    />

                                </div>
                            </Panel>) : (<Panel header={noClassificationsPanelHeader}>
                                <div><i>Please use search to add concepts</i></div>
                            </Panel>)
                        }
                    </Col>
                </div>
            </div>
        );

        return (
            <div>
                {this.props.processing ?
                    loadingGif
                    :
                    articleContainer
                }
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        processing: getProcessing(state),
        article: getArticle(state),
        journal: getJournal(state),
        taxonomies: getTaxonomies(state),
        concepts: getConcepts(state),
        treeConcepts: getTreeConcepts(state),
        humanConcepts: getHumanConcepts(state),
        loading: getLoading(state),
        conceptFilter: getConceptFilter(state),
        foundConcepts: getFoundConcepts(state),
        nextPageOffset: getNextPageOffset(state),
        nextPageLoading: getNextPageLoading(state),
        conceptProcessing: getConceptProcessing(state)
    }
}

export default connect(mapStateToProps, {
    articleLoadingSagaAC,
    conceptsSearchSagaAC,
    conceptsPageLoadingSagaAC,
    articleConceptRemovingSagaAC,
    articleHumanConceptRemovingSagaAC,
    foundConceptAdditionSagaAC,
    loadConceptChildrenSagaAC,
    loadTopConceptChildrenSagaAC,
    conceptsPageLoadingStartAC
})(Article)