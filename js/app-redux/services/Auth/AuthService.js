import $ from 'jquery';
import Q from 'q';
// import Promise from 'promise';


class AuthService {
  // We can avoid constructor with config and import here config, but if we have several different configs than it won't
  // work
  constructor(config) {
    this.config = config;
  }

  // Change to promises instead of Q when spr project will use them too?
  login = (login = '', password = '') => {

    // function sleep(time) {
    //   return new Promise((resolve) => setTimeout(resolve, time));
    // }
    //
    // return sleep(1000).then(() => {
    //   console.log('Inside AuthService.login ' + login + ' ' + password)
    // })
    console.debug('Inside AuthService.login() ' + login + ' ' + password);
    const defer = Q.defer();

    const url = `${this.config.loginApiUrl}`;
    /* eslint-disable quotes */
    const credentials = {
      "login": login,
      "password": password
    };
    /* eslint-enable quotes */

    // Change to fetch instead of $.ajax and try to avoid here then/fail logic and move this logic to saga? But if
    // we have some logic to manipulate data here we will have to move this to saga. Do it maybe if we spr will migrate
    // too
    $.ajax({
      url,
      method: 'POST',
      // contentType: 'application/json',
      // dataType: 'json',
      data: credentials
    })
      .then(payload => {
        console.debug('Payload inside AuthService.login ajax then(): ' + payload);
        defer.resolve(payload)
      })
      .fail(e => defer.reject(e));

    return defer.promise;
  };

  logout = () => {
    console.debug('Inside AuthService.logout()');
    const defer = Q.defer();

    $.ajax({
      url: `${this.config.logoutApiUrl}`,
      method: 'POST',
      data: ''
    })
      .then(payload => defer.resolve(payload))
      .fail(e => defer.reject(e));

    return defer.promise;
  };
}

export default AuthService;