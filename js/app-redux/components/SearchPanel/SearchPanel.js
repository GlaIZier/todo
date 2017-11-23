import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {Button, Col, ControlLabel, Form, FormControl, FormGroup, Panel, Radio} from 'react-bootstrap';
import './styles/searchPanel.css';
import commonConfig from '../../config/config.common';

class SearchPanel extends PureComponent {

  static propTypes = {
    filters: PropTypes.object.isRequired,
    processing: PropTypes.bool.isRequired,
    search: PropTypes.func.isRequired
  };

  // But for right now it's bound to props.
  constructor(props) {
    super(props);
    this.state = {
      journal: this.props.filters.journal,
      status: this.props.filters.status,
      doi: this.props.filters.doi,
      name: this.props.filters.name,
      sort: this.props.filters.sort
    };
  }

  handleJournalChange = (event) => {
    this.setState({journal: event.target.value});
  };

  handleStatusChange = (event) => {
    this.setState({status: event.target.value});
  };

  handleDoiChange = (event) => {
    this.setState({doi: event.target.value});
  };

  handleNameChange = (event) => {
    this.setState({name: event.target.value});
  };

  handleSortChange = (event) => {
    this.setState({sort: event.target.value});
  };

  handleSearch = (e) => {
    e.preventDefault();
    e.stopPropagation();

    this.props.search(this.state);
  };

  render() {

    const searchPanelTitle = (
      <h3>Find content</h3>
    );

    return (
      <div id="search" className="container">
        <Panel id="cc-search-panel-body" header={searchPanelTitle}>
          <Form horizontal onSubmit={this.handleSearch}>
            <FormGroup>
              <ControlLabel htmlFor="journal-acronym" className="col-sm-3 control-label">Journal title</ControlLabel>
              <Col md={9}>
                <FormControl
                  id="journal-acronym"
                  type="text"
                  placeholder="Enter journal title"
                  value={this.state.journal}
                  onChange={this.handleJournalChange}
                />
              </Col>
            </FormGroup>
            <FormGroup>
              <ControlLabel htmlFor="article-status" className="col-sm-3 control-label">Article status</ControlLabel>
              <Col md={9}>
                <FormControl
                  id="article-status"
                  componentClass="select"
                  placeholder="Select article status"
                  value={this.state.status}
                  onChange={this.handleStatusChange}
                >
                  <option value={commonConfig.constants.statusAll}>All</option>
                  <option value={commonConfig.constants.statusPublished}>Published</option>
                  <option value={commonConfig.constants.statusUnpublished}>Unpublished</option>
                </FormControl>
              </Col>
            </FormGroup>
            <FormGroup>
              <ControlLabel htmlFor="article-doi" className="col-sm-3 control-label">Article DOI</ControlLabel>
              <Col md={9}>
                <FormControl
                  id="article-doi"
                  type="text"
                  placeholder="Enter article DOI"
                  value={this.state.doi}
                  onChange={this.handleDoiChange}
                />
              </Col>
            </FormGroup>
            <FormGroup>
              <ControlLabel htmlFor="article-title" className="col-sm-3 control-label">Article title</ControlLabel>
              <Col md={9}>
                <FormControl
                  id="article-title"
                  type="text"
                  placeholder="Enter article title"
                  value={this.state.name}
                  onChange={this.handleNameChange}
                />
              </Col>
            </FormGroup>
            <FormGroup>
              <ControlLabel htmlFor="sort" className="col-sm-3 control-label">Sort by</ControlLabel>
              <Col md={9}>
                <Radio
                  name="radioGroup"
                  inline
                  value={'calc'}
                  checked={this.state.sort === commonConfig.constants.dateSort}
                  onChange={this.handleSortChange}
                >
                  Date
                </Radio>
                <Radio
                  name="radioGroup"
                  inline
                  value={'name'}
                  checked={this.state.sort === commonConfig.constants.nameSort}
                  onChange={this.handleSortChange}
                >
                  Title
                </Radio>
              </Col>
            </FormGroup>
            <FormGroup>
              <Col sm={9} smOffset={3}>
                <Button
                  className="btn-primary"
                  type="submit"
                  disabled={this.props.processing}>
                  Search
                </Button>
              </Col>
            </FormGroup>
          </Form>
        </Panel>
      </div>
    );
  }
}

export default SearchPanel;