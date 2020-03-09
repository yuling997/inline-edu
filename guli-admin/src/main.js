import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import '@/styles/index.scss' // global css

// 检查当前目录有没有App.vue 或 App.js文件
import App from './App'
// 检查当前目录有没有router.vue 或 router.js文件，
// 如果没有则检查当前目录有没有router/index.vue 或 router/index.js文件
import router from './router'
import store from './store'

import '@/icons' // icon
import '@/permission' // permission control

Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
