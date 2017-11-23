import React from 'react';
import PropTypes from 'prop-types';
import {ListGroup, ListGroupItem} from 'react-bootstrap';
import ConceptEdit from '../ConceptEdit';
import './styles/conceptNode.css';

class ConceptNode extends ConceptEdit {

    static propTypes = {

        // article id to work with
        articleId: PropTypes.string.isRequired,
        // article concepts
        concepts: PropTypes.array,
        // concept to add or remove depending on the value of assigned concept
        concept: PropTypes.object.isRequired,
        // concepts assigned to an article by human
        humanConcepts: PropTypes.array,
        // is the concept already assigned to the article?
        assignedConcept: PropTypes.bool,
        // is global processing taking place
        conceptProcessing: PropTypes.bool,

        // is this node a top concept?
        topConcept: PropTypes.bool,
        // tree structure of the taxonomy
        treeConcepts: PropTypes.object,
        // method to load top children
        loadTopChildren: PropTypes.func.isRequired,
        // method to load children
        loadChildren: PropTypes.func.isRequired,

        // node styles
        nodeButtonStyle: PropTypes.string.isRequired,
        nodeOpenedButtonStyle: PropTypes.string.isRequired,

        //styles
        addButtonStyle: PropTypes.string.isRequired,
        deleteButtonStyle: PropTypes.string.isRequired,

        //methods to add and to remove
        addConcept: PropTypes.func.isRequired,
        addHumanConcept: PropTypes.func.isRequired,
        deleteConcept: PropTypes.func.isRequired,
        deleteHumanConcept: PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            isHidden: true
        }
    }

    componentDidMount() {
        this.setState({isHidden: true});
    }

    toggleHidden() {
        this.setState({
            isHidden: !this.state.isHidden
        })
    }

    handleNodeClick = (e) => {
        e.preventDefault();
        e.stopPropagation();

        const nodeRedux = this.props.treeConcepts.get(this.props.concept.id);

        const childrenLoading = nodeRedux ? nodeRedux.loading : false;
        const childrenLoaded = nodeRedux ? nodeRedux.loaded : false;

        if (!childrenLoaded && !childrenLoading) {
            if (this.props.topConcept) {
                this.props.loadTopChildren(this.props.concept.id);
            } else {
                this.props.loadChildren(this.props.concept.id);
            }
        }
        this.toggleHidden();
    };

    render() {

        const articleId = this.props.articleId;

        const concepts = this.props.concepts;
        const humanConcepts = this.props.humanConcepts;
        const treeConcepts = this.props.treeConcepts;

        const conceptProcessing = this.props.conceptProcessing;

        const nodeButtonStyle = this.props.nodeButtonStyle;
        const nodeOpenedButtonStyle = this.props.nodeOpenedButtonStyle;

        const loadChildren = this.props.loadChildren;
        const loadTopChildren = this.props.loadTopChildren;

        const nodeRedux = this.props.treeConcepts.get(this.props.concept.id);

        const childrenLoading = nodeRedux ? nodeRedux.loading : false;
        const childrenConcepts = nodeRedux ? nodeRedux.children.toJS() : [];
        const childrenLoaded = nodeRedux ? nodeRedux.loaded : false;

        const addButtonStyle= this.props.addButtonStyle;
        const deleteButtonStyle= this.props.deleteButtonStyle;

        const addConcept= this.props.addConcept;
        const addHumanConcept= this.props.addHumanConcept;
        const deleteConcept= this.props.deleteConcept;
        const deleteHumanConcept= this.props.deleteHumanConcept;

        const classificationAddRemoveButton = this.createActionButton();

        const treeButton = (<span>
          {(childrenLoaded && !this.state.isHidden) ?
              (<span onClick={this.handleNodeClick}
                     className={`cc-concept-node-button glyphicon ${this.props.nodeOpenedButtonStyle}`}/>)
              :
              (<span onClick={this.handleNodeClick}
                     className={`cc-concept-node-button glyphicon ${this.props.nodeButtonStyle}`}/>)
          }
        </span>);

        const node = (
            <div style={{wordWrap: 'break-word'}}>
                {childrenLoading ?
                    (<span className='cc-concept-node-glyph glyphicon glyphicon-time'/>)
                    :
                    treeButton
                }
                {this.props.concept.prefLabel}
                {(!this.props.topConcept) ? classificationAddRemoveButton : ''}
            </div>
        );


        const children = (childrenConcepts && childrenConcepts.length > 0) ? (
            <ListGroup>
                {childrenConcepts.map(function (concept, i) {

                    let alreadyThere = false;
                    concepts.map(function (articleConcept) {
                        if (articleConcept.id === concept.id) {
                            alreadyThere = true;
                        }
                    });

                    return (concept.hasChildren) ? (<ListGroupItem key={i} bsStyle={alreadyThere ? 'success' : null}>
                            <ConceptNode
                                articleId={articleId}
                                concepts={concepts}
                                concept={concept}

                                humanConcepts={humanConcepts}
                                assignedConcept={alreadyThere}
                                conceptProcessing={conceptProcessing}

                                topConcept={false}
                                treeConcepts={treeConcepts}
                                loadTopChildren={loadTopChildren}
                                loadChildren={loadChildren}

                                nodeButtonStyle={nodeButtonStyle}
                                nodeOpenedButtonStyle={nodeOpenedButtonStyle}

                                addButtonStyle={addButtonStyle}
                                deleteButtonStyle={deleteButtonStyle}

                                addConcept={addConcept}
                                addHumanConcept={addHumanConcept}
                                deleteConcept={deleteConcept}
                                deleteHumanConcept={deleteHumanConcept}
                               />
                        </ListGroupItem>)
                        :
                        (<ListGroupItem key={i}>
                            <ConceptEdit
                                articleId={articleId}
                                concept={concept}
                                humanConcepts={humanConcepts}

                                assignedConcept={alreadyThere}
                                conceptProcessing={conceptProcessing}

                                addButtonStyle={addButtonStyle}
                                deleteButtonStyle={deleteButtonStyle}

                                addConcept={addConcept}
                                addHumanConcept={addHumanConcept}
                                deleteConcept={deleteConcept}
                                deleteHumanConcept={deleteHumanConcept}
                            /></ListGroupItem>)
                })}
            </ListGroup>
        ) : ((!childrenLoading && this.props.topConcept) ? <span>
            No concepts in this component
        </span> : '');

        return (
            <span>
        {node}
                {!this.state.isHidden && children}

     </span>
        );

    }
}

export default ConceptNode;