const webpack = require('webpack');
const webpackDevMiddleware = require('webpack-dev-middleware');
const webpackHotMiddleware = require('webpack-hot-middleware');
const getConfig = require('./webpack.config');

// const express = require('express');
// const app = new (require('express'))()
const express = require('express');
const path = require('path');

// Todo try to create https express
const app = express();
const port = 3000;

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

/*
// Search. Journals
app.post('/ks/v2/Journals/search', function (req, res) {
  // path.join(__dirname, '../public', 'index1.html')
  res.sendFile(path.join(__dirname, '/data', 'search-journals.json'));
});

// Search. Articles
app.post('/ks/v2/ArticleProducts/search', function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'search-articles.json'));
});


// Article. Journal for article
app.get(/^\/ks\/v2\/ArticleProducts\/(.*)\/Journals\/isPartOfJournal(.*)/, function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'article-journal.json'));
});

// Article. Taxonomies for journal
app.get(/^\/ks\/v2\/Journals\/(.*)\/ConceptSchemes\/describedWithConceptScheme/, function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'article-journal-taxonomies.json'));
});

// Article. Search concepts in taxonomies
app.post('/ks/v2/Concepts/search', function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'article-search-concepts.json'));
});

// Any concept. Child concepts.
app.get(/^\/ks\/v2\/Concepts\/(.*)\/Concepts\/narrower(.*)/, function (req, res) {
    res.sendFile(path.join(__dirname, '/data', 'concept-children.json'));
});

// Article. Concepts.
app.get(/^\/ks\/v2\/ConceptSchemes\/(.*)\/Concepts\/hasTopConcept(.*)/, function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'taxonomies-top-concepts.json'));
});

// top. Concepts.
app.get(/^\/ks\/v2\/ArticleProducts\/(.*)\/Concepts\/classifiedWithConcept(.*)/, function (req, res) {
    res.sendFile(path.join(__dirname, '/data', 'article-concepts.json'));
});


app.delete(/^\/ks\/v2\/ArticleProducts\/(.*)\/Concepts\/(.*)\/classifiedWithConcept(.*)/, function (req, res) {
    res.status(204);
    res.sendFile(path.join(__dirname, '/data', 'concept-edit.txt'));
});

app.put(/^\/ks\/v2\/ArticleProducts\/(.*)\/Concepts\/(.*)\/classifiedByHuman(.*)/, function (req, res) {
  res.status(200);
  res.contentType('text/plain');
  res.sendFile(path.join(__dirname, '/data', 'concept-edit.txt'));
});

app.delete(/^\/ks\/v2\/ArticleProducts\/(.*)\/Concepts\/(.*)\/classifiedByHuman(.*)/, function (req, res) {
  res.status(204);
  res.sendFile(path.join(__dirname, '/data', 'concept-edit.txt'));
});

// Article
app.get(/^\/ks\/v2\/ArticleProducts\/(.*)/, function (req, res) {
  res.sendFile(path.join(__dirname, '/data', 'article.json'));
});

 */
// Web application for all other urls
app.get(/.*/, function (req, res) {
  res.sendFile(__dirname + '/app-redux/index.html')
});

// app.use('/images/', express.static(__dirname + '/app-redux/images'));

app.listen(port, function (error) {
  if (error) {
    console.error(error)
  } else {
    console.info("==> 🌎  Listening on port %s. Open up http://localhost:%s/ in your browser.", port, port)
  }
});