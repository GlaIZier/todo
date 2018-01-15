import React, {PureComponent} from 'react';
import {Task} from '../../containers/Task';


class TaskPage extends PureComponent {
  render() {
    return (
      <Task
        taskId={this.props.params.taskId}
      />
    );
  }
}

export default TaskPage;