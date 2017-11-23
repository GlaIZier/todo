import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {ListGroup, ListGroupItem} from 'react-bootstrap';
import ConceptEdit from '../ConceptEdit';

class ConceptsList extends PureComponent {

    static propTypes = {
        // article id to work with
        articleId: PropTypes.string.isRequired,

        concepts: PropTypes.array,
        // concepts assigned to an article all
        articleConcepts: PropTypes.array,
        // concepts assigned to an article by human
        humanConcepts: PropTypes.array,

        // is global processing taking place
        conceptProcessing: PropTypes.bool,

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
        const conceptProcessing = this.props.conceptProcessing;

        const articleConcepts = this.props.articleConcepts;
        const humanConcepts = this.props.humanConcepts;

        const addButtonStyle= this.props.addButtonStyle;
        const deleteButtonStyle= this.props.deleteButtonStyle;

        const addConcept= this.props.addConcept;
        const addHumanConcept= this.props.addHumanConcept;
        const deleteConcept= this.props.deleteConcept;
        const deleteHumanConcept= this.props.deleteHumanConcept;

        return (
            <ListGroup>
                {this.props.concepts.map(function (concept, i) {

                    let assignedConcept = false;

                    if (Array.isArray(articleConcepts)) {
                        for (const articleConcept of articleConcepts) {
                            if (articleConcept.id === concept.id) {
                                assignedConcept = true;
                                break;
                            }
                        }
                    }

                    return <ListGroupItem key={i}>
                        <ConceptEdit
                            articleId={articleId}
                            concept={concept}
                            humanConcepts={humanConcepts}

                            assignedConcept={assignedConcept}
                            conceptProcessing={conceptProcessing}

                            addButtonStyle={addButtonStyle}
                            deleteButtonStyle={deleteButtonStyle}

                            addConcept={addConcept}
                            addHumanConcept={addHumanConcept}
                            deleteConcept={deleteConcept}
                            deleteHumanConcept={deleteHumanConcept}
                        />
                    </ListGroupItem>
                })}
            </ListGroup>
        )
            ;
    }
}

export default ConceptsList;