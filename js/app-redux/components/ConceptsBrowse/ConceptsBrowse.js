import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {ListGroup, ListGroupItem} from 'react-bootstrap';
import ConceptsNode from '../ConceptsNode';

class ConceptsBrowse extends PureComponent {

    static propTypes = {
        // article id to work with
        articleId: PropTypes.string.isRequired,
        // journal taxonomies
        taxonomies: PropTypes.array.isRequired,
        // article concepts
        concepts: PropTypes.array,
        // concepts assigned to an article by human
        humanConcepts: PropTypes.array,

        // is global processing taking place
        conceptProcessing: PropTypes.bool,

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
    }

    render() {

        const articleId = this.props.articleId;
        const nodeButtonStyle = this.props.nodeButtonStyle;
        const nodeOpenedButtonStyle = this.props.nodeOpenedButtonStyle;

        const loadChildren = this.props.loadChildren;
        const conceptProcessing = this.props.conceptProcessing;
        const loadTopChildren = this.props.loadTopChildren;

        const concepts = this.props.concepts;
        const humanConcepts = this.props.humanConcepts;
        const treeConcepts = this.props.treeConcepts;

        const addButtonStyle= this.props.addButtonStyle;
        const deleteButtonStyle= this.props.deleteButtonStyle;

        const addConcept= this.props.addConcept;
        const addHumanConcept= this.props.addHumanConcept;
        const deleteConcept= this.props.deleteConcept;
        const deleteHumanConcept= this.props.deleteHumanConcept;

        return (
            <ListGroup>
                {
                    this.props.taxonomies.map(function (taxonomy, i) {
                        return (
                            <ListGroupItem key={i}>
                                <ConceptsNode
                                    articleId={articleId}
                                    concepts={concepts}
                                    concept={taxonomy}

                                    humanConcepts={humanConcepts}
                                    assignedConcept={false}
                                    conceptProcessing={conceptProcessing}

                                    topConcept={true}
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
                            </ListGroupItem>
                        );
                    })
                }
            </ListGroup>
        )
    }
}

export default ConceptsBrowse;