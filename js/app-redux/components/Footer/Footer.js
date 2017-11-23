import React, {PureComponent} from 'react';
import './styles/footer.css';

class Footer extends PureComponent {

  render() {
    return (
      <div className='footer'>
        <div className='container'>
          <p>&copy; 2017 John Wiley &amp; Sons, Ltd</p>
        </div>
      </div>
    );
  }
}

export default Footer;