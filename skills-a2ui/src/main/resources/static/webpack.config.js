/**
 * Webpack 配置 - OOD ES6 模块化
 * 
 * 支持 ES6 模块与传统脚本的混合构建
 */

const path = require('path');
const webpack = require('webpack');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = (env, argv) => {
  const isProduction = argv.mode === 'production';
  const isDevelopment = !isProduction;

  return {
    // 模式
    mode: isProduction ? 'production' : 'development',

    // 入口点
    entry: {
      // ES6 主入口
      'ood-es6': {
        import: './ood/es6/index.js',
        filename: isProduction ? 'ood-es6.min.js' : 'ood-es6.js',
      },
      // 兼容层入口（用于旧代码）
      'ood-compat': {
        import: './ood/es6/index.js',
        filename: isProduction ? 'ood-compat.min.js' : 'ood-compat.js',
      },
    },

    // 输出
    output: {
      path: path.resolve(__dirname, 'dist'),
      filename: '[name].js',
      library: 'ood',
      libraryTarget: 'umd',
      globalObject: 'typeof self !== "undefined" ? self : this',
      umdNamedDefine: true,
      // 生产环境使用 contenthash
      chunkFilename: isProduction ? '[name].[contenthash:8].js' : '[name].js',
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
        'process.env.NODE_ENV': JSON.stringify(isProduction ? 'production' : 'development'),
        'process.env.VERSION': JSON.stringify(require('./package.json').version || '0.5.0'),
      }),

      // 复制静态资源
      new CopyWebpackPlugin({
        patterns: [
          {
            from: 'ood/css',
            to: 'css',
          },
          {
            from: 'ood/appearance',
            to: 'appearance',
          },
          {
            from: 'ood/iconfont',
            to: 'iconfont',
          },
          {
            from: 'ood/img',
            to: 'img',
          },
        ],
      }),

      // 生产环境优化
      ...(isProduction
        ? [
            new webpack.optimize.ModuleConcatenationPlugin(),
          ]
        : []),
    ],

    // 开发服务器（仅开发模式）
    devServer: isDevelopment
      ? {
          static: {
            directory: path.join(__dirname),
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
        }
      : undefined,

    // 源映射
    devtool: isDevelopment ? 'eval-source-map' : 'source-map',

    // 优化
    optimization: {
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
      // 生产环境压缩
      minimize: isProduction,
      // 运行时代码单独提取
      runtimeChunk: 'single',
    },

    // 性能提示
    performance: {
      hints: isProduction ? 'warning' : false,
      maxEntrypointSize: 512000,
      maxAssetSize: 512000,
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
};
