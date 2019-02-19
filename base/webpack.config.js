const path = require('path');

module.exports = {
  mode: 'development',
  output: {
    filename: 'index.js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: { loader: 'babel-loader' }
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[path][name].[ext]',
              context: 'src/main/webpack',
            },
          },
          { loader: 'extract-loader', },
          { loader: 'css-loader', },
        ]
      },
      {
        test: /\.(scss)$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[path][name].css',
              context: 'src/main/webpack',
            },
          },
          { loader: 'extract-loader', },
          { loader: 'css-loader', },
          {
            loader: 'postcss-loader',
            options: {
              plugins: function () {
                return [
                  require('autoprefixer')
                ];
              }
            }
          },
          { loader: 'sass-loader' }
        ]
      },
      {
        test: /\.(png|jpg|jpeg|gif)$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[path][name].[ext]',
              context: 'src/main/webpack',
            },
          },
        ],
      },
      {
        test: /\.(htm|html)$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[path][name].[ext]',
              context: 'src/main/webpack',
            },
          },
          { loader: 'extract-loader', },
          {
            loader: 'html-loader',
            options: {
              interpolate: 'require',
            }
          },
        ]
      },
      {
        test: /\.htmli$/,
        use: [
          {
            loader: 'html-loader',
            options: {
              interpolate: 'require',
            }
          },
        ]
      },
    ]
  }
};
