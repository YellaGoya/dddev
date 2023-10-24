const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin');

const isDevelopment = process.env.NODE_ENV !== 'production';

module.exports = () => {
  return {
    mode: isDevelopment ? 'development' : 'production',
    entry: { app: './src/index.tsx' },
    output: { path: path.resolve(__dirname, './dist') },
    module: {
      rules: [
        {
          test: /\.tsx$/,
          exclude: /node_modules/,
          use: {
            loader: 'babel-loader',
            options: {
              plugins: [isDevelopment && 'react-refresh/babel'].filter(Boolean),
            },
          },
        },
      ],
    },
    resolve: { extensions: ['.ts', '.tsx', '.js'] },
    devServer: {
      hot: true,
      port: 9000,
    },
    plugins: [
      isDevelopment && new ReactRefreshWebpackPlugin(),
      new HtmlWebpackPlugin({
        template: './src/index.html',
      }),
    ].filter(Boolean),
  };
};
