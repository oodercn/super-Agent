/**
 * Webpack 生产配置
 */

const { merge } = require('webpack-merge');
const baseConfig = require('./webpack.config.js');
const TerserPlugin = require('terser-webpack-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');

module.exports = merge(baseConfig, {
  mode: 'production',

  devtool: 'source-map',

  output: {
    filename: '[name].min.js',
    chunkFilename: '[name].[contenthash:8].min.js',
  },

  optimization: {
    minimize: true,
    minimizer: [
      new TerserPlugin({
        terserOptions: {
          parse: {
            ecma: 8,
          },
          compress: {
            ecma: 5,
            warnings: false,
            comparisons: false,
            inline: 2,
            drop_console: true,
            drop_debugger: true,
          },
          mangle: {
            safari10: true,
          },
          output: {
            ecma: 5,
            comments: false,
            ascii_only: true,
          },
        },
        parallel: true,
      }),
      new CssMinimizerPlugin(),
    ],
  },

  performance: {
    hints: 'warning',
    maxEntrypointSize: 256000,
    maxAssetSize: 256000,
  },
});
