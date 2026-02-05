/**
 * Webpack 开发配置
 */

const { merge } = require('webpack-merge');
const baseConfig = require('./webpack.config.js');

module.exports = merge(baseConfig, {
  mode: 'development',

  devtool: 'eval-cheap-module-source-map',

  output: {
    filename: '[name].js',
    chunkFilename: '[name].js',
  },

  devServer: {
    static: {
      directory: require('path').join(__dirname),
    },
    compress: true,
    port: 8080,
    hot: true,
    open: true,
    historyApiFallback: true,
    client: {
      overlay: {
        errors: true,
        warnings: false,
      },
    },
  },

  optimization: {
    minimize: false,
  },

  performance: {
    hints: false,
  },
});
