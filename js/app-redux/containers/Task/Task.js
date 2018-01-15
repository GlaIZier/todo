import React, {PureComponent} from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import './styles/task.css';

class Task extends PureComponent {

  static propTypes = {
    taskId: PropTypes.string.isRequired,
  };

  render() {
    return (
      <div>
        {this.props.taskId}
      </div>
    )
  }
}

function mapStateToProps(state) {
  console.log(`mapStateToProps ${state}`);
  return {}
}

export default connect(mapStateToProps, {})(Task)