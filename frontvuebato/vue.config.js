publicPath: process.env.NODE_ENV === 'production'
module.exports = {
  lintOnSave: false,
  transpileDependencies: [
    'vuetify',
  ],
  publicPath: process.env.NODE_ENV === 'production' ? '/frontvuebato' : '/'
};
