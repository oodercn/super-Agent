/**
 * Webpack 发布配置 - OOD ES6 模块化
 * 
 * 专为发布版本配置的构建
 */

const path = require('path');
const webpack = require('webpack');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');

module.exports = {
  // 模式
  mode: 'production',

  // 入口点
  entry: {
    // ES6 主入口
    'ood-es6': {
      import: './ood/es6/index.js',
      filename: 'ood-es6.min.js',
    },
    // 兼容层入口（用于旧代码）
    'ood-compat': {
      import: './ood/es6/index.js',
      filename: 'ood-compat.min.js',
    },
  },

  // 输出
  output: {
    path: path.resolve(__dirname, 'dist/release/es6-modules'),
    filename: '[name].min.js',
    library: 'ood',
    libraryTarget: 'umd',
    globalObject: 'typeof self !== "undefined" ? self : this',
    umdNamedDefine: true,
    // 生产环境使用 contenthash
    chunkFilename: '[name].[contenthash:8].min.js',
  },

  // 模块解析
  resolve: {
    extensions: ['.js', '.mjs', '.json'],
    alias: {
      '@ood': path.resolve(__dirname, './ood'),
      '@modules': path.resolve(__dirname, './ood/modules'),
      '@es6': path.resolve(__dirname, './ood/es6'),
      '@legacy': path.resolve(__dirname, './ood/js'),
    },
    // 默认使用 ES 模块语法
    mainFields: ['module', 'main'],
  },

  // 外部依赖（不打包，继续使用全局变量）
  externals: {
    // 如果需要保留某些第三方库的外部引用
    // 'jquery': 'jQuery',
    // 'raphael': 'Raphael',
  },

  // 模块规则
  module: {
    rules: [
      // ES6 模块（使用 Babel 转译）
      {
        test: /\.m?js$/,
        include: [
          path.resolve(__dirname, './ood/es6'),
          path.resolve(__dirname, './ood/modules'),
        ],
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: [
              [
                '@babel/preset-env',
                {
                  targets: {
                    browsers: ['> 0.5%', 'last 2 versions', 'not dead'],
                  },
                  useBuiltIns: 'usage',
                  corejs: 3,
                  modules: false, // 保持 ES6 模块
                },
              ],
            ],
            plugins: [
              '@babel/plugin-proposal-class-properties',
              '@babel/plugin-proposal-object-rest-spread',
              '@babel/plugin-proposal-decorators',
            ],
          },
        },
      },
      // 传统脚本（原样加载）
      {
        test: /ood\/js\/.*\.js$/,
        include: [path.resolve(__dirname, './ood/js')],
        use: {
          loader: 'script-loader',
          options: {
            // 标记为传统脚本
            type: 'script',
          },
        },
      },
      // 加载 CSS
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },

  // 插件
  plugins: [
    // 清理输出目录
    new CleanWebpackPlugin({
      cleanStaleWebpackAssets: false,
      cleanOnceBeforeBuildPatterns: ['**/*'],
    }),

    // 全局常量
    new webpack.DefinePlugin({
      'process.env.NODE_ENV': JSON.stringify('production'),
      'process.env.VERSION': JSON.stringify(require('./package.json').version || '0.5.0'),
    }),


  ],

  // 源映射
  devtool: 'source-map',

  // 优化
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
    // 代码分割
    splitChunks: {
      chunks: 'all',
      cacheGroups: {
        // 兼容层单独打包
        compat: {
          test: /[\\/]ood[\\/]es6[\\/]/,
          name: 'compat',
          priority: 10,
          reuseExistingChunk: true,
        },
        // 公共依赖单独打包
        vendors: {
          test: /[\\/]node_modules[\\/]/,
          name: 'vendors',
          priority: 5,
          reuseExistingChunk: true,
        },
        // 默认组
        default: {
          minChunks: 2,
          priority: -10,
          reuseExistingChunk: true,
        },
      },
    },
    // 运行时代码单独提取
    runtimeChunk: 'single',
  },

  // 性能提示
  performance: {
    hints: 'warning',
    maxEntrypointSize: 256000,
    maxAssetSize: 256000,
  },

  // 统计信息
  stats: {
    colors: true,
    hash: false,
    version: false,
    timings: true,
    assets: true,
    chunks: false,
    modules: false,
    reasons: false,
    children: false,
    source: false,
    errors: true,
    errorDetails: true,
    warnings: true,
    publicPath: false,
  },

  // 缓存
  cache: {
    type: 'filesystem',
    buildDependencies: {
      config: [__filename],
    },
  },
};