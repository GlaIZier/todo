import $ from 'jquery';
import Q from 'q';

// import Promise from 'promise';


class RegisterService {
  // We can avoid constructor with config and import here config, but if we have several different configs than it won't
  // work
  constructor(config) {
    this.config = config;
  }

  // Change to promises instead of Q when spr project will use them too?
  register = (login = '', password = '') => {
    const defer = Q.defer();

    const url = `${this.config.registerApiUrl}`;
    /* eslint-disable quotes */
    const credentials = {
      "login": login,
      "password": password
    };
    /* eslint-enable quotes */

    $.ajax({
      url,
      method: 'POST',
      data: credentials
    })
      .then(payload => {
        defer.resolve(payload)
      })
      .fail(e => defer.reject(e));

    return defer.promise;
  };
}

export default RegisterService;