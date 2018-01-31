const webpack = require('webpack');
const webpackDevMiddleware = require('webpack-dev-middleware');
const webpackHotMiddleware = require('webpack-hot-middleware');
const getConfig = require('./webpack.config');

const express = require('express');
const path = require('path');

const fs = require('fs');
const http = require('http');
const https = require('https');
// const express = require('express');
// const app = new (require('express'))()


const app = express();
const port = 3000;
const httpsPort = 3443;

let config;
if (process.argv[2]) {
  config = getConfig(process.argv[2]);
} else {
  config = getConfig('LOCAL');
}

const compiler = webpack(config);
app.use(webpackDevMiddleware(compiler, {noInfo: true, publicPath: config.output.publicPath}));
app.use(webpackHotMiddleware(compiler));

// Fake 1 sec latency for server's response as middleware
app.use(function (req, res, next) {
  setTimeout(next, 1000)
});

// Auth
app.post('/todo/api/auth/login', function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'auth-login-mock-response.json'));
});

app.post('/todo/api/auth/me/logout', function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'auth-logout-mock-response.json'));
});

// Tasks
app.get('/todo/api/me/tasks', function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'tasks-get-mock-response.json'));
});

app.post('/todo/api/me/tasks', function (req, res) {
  res.status(201);
  res.sendFile(path.join(__dirname, '/data', 'task-post-mock-response.json'));
});

app.put(/^\/todo\/api\/me\/tasks\/(.*)/, function (req, res) {
  res.status(200);
  res.sendFile(path.join(__dirname, '/data', 'task-put-mock-response.json'));
});

app.delete(/^\/todo\/api\/me\/tasks\/(.*)/, function (req, res) {
  res.status(200);
  res.sendFile(path.join(__dirname, '/data', 'task-delete-mock-response.json'));
});

app.get(/^\/todo\/api\/me\/tasks\/(.*)/, function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'task-get-mock-response.json'));
});

// Users
app.post('/todo/api/users', function (req, res) {
  res.status(201);
  res.sendFile(path.join(__dirname, '/data', 'user-post-mock-response.json'));
});

// Web application for all other urls
app.get(/todo\/spa.*/, function (req, res) {
  res.sendFile(__dirname + '/app-redux/index.html')
});

// Todo fix it
// app.use('/todo/resources', express.static(path.join(__dirname, '../src/main/webapp/resources')));
// app.use('/static', express.static(path.join(__dirname, 'data')));


const privateKey = fs.readFileSync('keys/express.key', 'utf8');
const certificate = fs.readFileSync('keys/express.crt', 'utf8');
const credentials = {key: privateKey, cert: certificate};

const httpServer = http.createServer(app);
const httpsServer = https.createServer(credentials, app);

httpServer.listen(port, function (error) {
  if (error) {
    console.error(error)
  } else {
    console.info("HTTP ==> 🌎 Listening on port %s. Open up http://localhost:%s/ in your browser.", port, port)
  }
});

httpsServer.listen(httpsPort, function (error) {
  if (error) {
    console.error(error)
  } else {
    console.info("HTTPS ==> 🌎  Listening on port %s. Open up https://localhost:%s/ in your browser.", httpsPort, httpsPort)
  }
});

// app.listen(port, function (error) {
//   if (error) {
//     console.error(error)
//   } else {
//     console.info("==> 🌎  Listening on port %s. Open up http://localhost:%s/ in your browser.", port, port)
//   }
// });