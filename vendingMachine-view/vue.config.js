module.exports = {
  configureWebpack: {
    resolve: {
      alias: {
        'vue$': 'vue/dist/vue.common.js'
      }
    }
  },
  pages: {
    app: {
      entry: 'src/App/app.ts',
      template: 'public/app.html',
      filename: 'app.html'
    },
    manager: {
      entry: 'src/Manager/manager.ts',
      template: 'public/manager.html',
      filename: 'manager.html'
    }
  },
  devServer: {
    port: 8081,
    historyApiFallback: {
      rewrites: [
        { from: /\/app/, to: '/app.html' },
        { from: /\/manager/, to: '/manager.html' },
      ]
    }
  }
}
