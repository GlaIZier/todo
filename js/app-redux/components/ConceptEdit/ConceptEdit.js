import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import './styles/conceptEdit.css';

class ConceptEdit extends PureComponent {

    static propTypes = {

        // article id to work with
        articleId: PropTypes.string.isRequired,
        // concept to add or remove depending on the value of assigned concept
        concept: PropTypes.object.isRequired,
        // concepts assigned to an article by human
        humanConcepts: PropTypes.array,

        // is the concept already assigned to the article?
        assignedConcept: PropTypes.bool,
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

        let alreadyThere = false;
        const concept = this.props.concept;

        this.props.humanConcepts.map(function (articleConcept) {
            if (articleConcept.id === concept.id) {
                alreadyThere = true;
            }
        });

        this.state = {
            humanConcept: alreadyThere
        }
    }

    handleClick = (e) => {
        e.preventDefault();
        e.stopPropagation();

        if (!this.props.conceptProcessing) {
            if (this.props.assignedConcept) {
                if (this.state.humanConcept) {
                    this.props.deleteHumanConcept(this.props.articleId, this.props.concept);
                } else {
                    this.props.deleteConcept(this.props.articleId, this.props.concept);
                }
            } else {
                if (this.state.humanConcept) {
                    this.props.addHumanConcept(this.props.articleId, this.props.concept);
                } else {
                    this.props.addConcept(this.props.articleId, this.props.concept);
                }
            }
        }
    };

    createActionButton = () => {

        let alreadyThere = this.props.assignedConcept;

        const classificationButton = (
            this.props.conceptProcessing ?
                (<span className='cc-concept-edit-icon glyphicon glyphicon-time'/>)
                :
                (<span onClick={this.handleClick}
                       className={`cc-concept-edit-icon cc-concept-edit-button glyphicon ${this.props.addButtonStyle}`}/>)
        );

        const removeClassificationButton = (
            this.props.conceptProcessing ?
                (<span className='cc-concept-edit-icon glyphicon glyphicon-time'/>)
                :
                (<span onClick={this.handleClick}
                       className={`cc-concept-edit-icon cc-concept-edit-button glyphicon ${this.props.deleteButtonStyle}`}/>)
        );

        return alreadyThere ? removeClassificationButton : classificationButton

    };

    render() {

        const classificationAddRemoveButton = this.createActionButton();

        return (
            <div style={{wordWrap: 'break-word'}}>
                {this.props.concept.prefLabel}
                {classificationAddRemoveButton}
            </div>
        );
    }
}

export default ConceptEdit;