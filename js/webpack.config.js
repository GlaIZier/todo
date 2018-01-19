const path = require('path');
const webpack = require('webpack');
const merge = require('webpack-merge');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');


const APP_ENTRY_POINT = './app-redux/index.js';
// output path for packed bundle content.
const OUTPUT_PATH = path.join(__dirname, '../src/main/webapp/resources/js/spa');
const APP_FOLDER = path.resolve(__dirname, "app-redux");

// Todo update readme for spa
module.exports = (env = 'LOCAL') => {
  console.log(`Bundling with ${env} environment...`);
  // server will use this path to output bundled static content: localhost:3000/dist/spa.js;
  // localhost/dist/images/loading.gif
  // const PUBLIC_PATH = (env !== 'LOCAL') ? '/todo/' : '/';
  const PUBLIC_PATH = '/todo/spa/';

  let config = {
    entry: [
      'babel-polyfill',
      APP_ENTRY_POINT
    ],
    output: {
      path: OUTPUT_PATH,
      filename: 'spa.js',
      publicPath: PUBLIC_PATH
    },
    plugins: [
      new webpack.DefinePlugin({
        __LOCAL__: env === 'LOCAL',
        __DEV__: env === 'DEV',
        __PROD__: env === 'PROD'
      }),
      // Assign the module and chunk ids by occurrence count. Ids that are used often get lower (shorter) ids.
      // This make ids predictable, reduces total file size and is recommended
      new webpack.optimize.OccurrenceOrderPlugin(),
      // Doesn't let rewrite scripts when they contain errors
      new webpack.NoEmitOnErrorsPlugin(),
      // This plugin automatically load modules instead of having to import or require them everywhere
      // It is needed to make bootstrap.min.js work with jquery
      new webpack.ProvidePlugin({
        jQuery: 'jquery',
        $: 'jquery',
        jquery: 'jquery'
      })
    ],
    module: {
      rules: [
        {
          test: /\.jsx?$/, // both .js and .jsx
          loader: 'eslint-loader',
          include: APP_FOLDER,
          enforce: 'pre',
        },
        {
          loader: 'babel-loader',
          include: [
            APP_FOLDER,
          ],
          test: /\.js$/,
          options: {
            // We can define code from babelrc here
            /*
             Babel uses very small helpers for common functions such as _extend. By default this will be
             added to every file that requires it. This duplication is sometimes unnecessary, especially when your
             application is spread out over multiple files.
             This is where the transform-runtime plugin comes in: all of the helpers will reference the module
             babel-runtime to avoid duplication across your compiled output. The runtime will be compiled into your build.
             */
            plugins: ['transform-runtime']
          }
        },
        {
          test: /\.(png|jpg|gif)$/,
          loader: 'file-loader',
          options: {
            name: 'images/[name].[ext]',
          },
        },
        {
          test: /\.css$/,
          loader: "style-loader!css-loader"
        },
        {
          test: /\.woff($|\?)|\.woff2($|\?)|\.ttf($|\?)|\.eot($|\?)|\.svg($|\?)/,
          loader: 'url-loader'
        }
      ],
    }
  };

  if (env !== 'PROD') {
    console.log('Non-prod profile has been found in webpack');
    if (env === 'LOCAL') {
      // adding hot-middleware client to entry after babel but before APP_ENTRY_POINT
      config.entry.splice(1, 0, 'webpack-hot-middleware/client');
      // Webpack adds a small HMR runtime to the bundle, during the build process, that runs inside your app.
      // When the build completes, Webpack does not exit but stays active, watching the source files for changes.
      // If Webpack detects a source file change, it rebuilds only the changed module(s).
      config.plugins.push(new webpack.HotModuleReplacementPlugin());
      console.log('Local profile has been found in webpack. Hot reloading middleware has been added')
    }

    // Uncomment if u need final spa.js script file lightweight
    // return merge(config, {
    //     devtool: 'cheap-module-eval-source-map'
    // });
    return config;

  } else {
    console.log('Prod profile has been found in webpack');
    // Makes it unreadable
    return merge(config, {
      plugins: [
        new webpack.DefinePlugin({
          'process.env': {
            NODE_ENV: JSON.stringify('production')
          }
        }),
        new UglifyJSPlugin()
      ]
    });
  }

};