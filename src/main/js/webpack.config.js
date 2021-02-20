const ESLintPlugin = require('eslint-webpack-plugin');

const path = require('path');

module.exports = {
  entry: './src/index.js',
  plugins: [new ESLintPlugin()],
  devtool: "source-map",
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist'),
    libraryTarget: 'umd'
  },
};
