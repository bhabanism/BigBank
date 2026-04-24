const path = require('path');
const { shareAll, withModuleFederationPlugin } = require('@angular-architects/module-federation/webpack');

const merge = withModuleFederationPlugin({
  shared: {
    ...shareAll({ singleton: true, strictVersion: true, requiredVersion: 'auto' }),
  },
});

/** Reduces Windows/Webpack watch loops from dist, cache, and the sibling MFE app. */
function applyWatchOptions(merged) {
  merged.devServer = merged.devServer || {};
  merged.devServer.watchOptions = {
    ...merged.devServer.watchOptions,
    ignored: [
      /[\\/]node_modules[\\/]/,
      /[\\/]dist[\\/]/,
      /[\\/]\.angular[\\/]/,
      path.join(__dirname, '../../dist'),
      path.join(__dirname, '../../.angular'),
      path.join(__dirname, '../bank-admin-mfe'),
    ],
    aggregateTimeout: 600,
  };
  return merged;
}

module.exports = (config, options) => {
  const out = merge(config, options);
  return applyWatchOptions(out);
};
