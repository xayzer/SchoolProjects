import Vue from 'vue'
import Vuex from 'vuex'
import App from './App.vue'

import Buefy from 'buefy'
import 'buefy/dist/buefy.css'
import '@mdi/font/css/materialdesignicons.css'

import router from './router'
import {http} from './axios'
import store from './store'

Vue.config.productionTip = false
Vue.prototype.$http = http

Vue.use(Buefy)
Vue.use(Vuex)

store.dispatch('init')

new Vue({
  render: h => h(App),  
  router,
  store
}).$mount('#app')
