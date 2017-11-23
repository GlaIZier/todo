import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup, InputGroup, Row} from 'react-bootstrap';
import './styles/textSearch.css';

class TextSearch extends PureComponent {

  static propTypes = {
    placeholder: PropTypes.string,
    search: PropTypes.func.isRequired,
    value: PropTypes.string.isRequired,
    loading: PropTypes.bool.isRequired
  };

  // But for right now it's bound to props.
  constructor(props) {
    super(props);
    this.state = {
      value: this.props.value,
    };
  }

  handleChange = (event) => {
    this.setState({value: event.target.value});
  };

  handleSearch = (e) => {
    e.preventDefault();
    e.stopPropagation();

    this.props.search(this.state.value);
  };

  render() {

    return (
      <Row id="cc-text-search" className="jumbotron">
        <Col md={8} smOffset={4}>
          <Form onSubmit={this.handleSearch}>
            <FormGroup id="cc-text-search-form-group">
              <InputGroup>
                <FormControl
                  type="text"
                  placeholder={this.props.placeholder}
                  value={this.state.value}
                  onChange={this.handleChange}
                />
                <InputGroup.Button>
                  <Button
                    className="btn-primary"
                    type="submit"
                    disabled={this.props.loading}
                  >
                    <span className="glyphicon glyphicon-search"/>
                  </Button>
                </InputGroup.Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
    );
  }
}

export default TextSearch;