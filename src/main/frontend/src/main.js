import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import DatetimePicker from 'vuetify-datetime-picker'

Vue.config.productionTip = false
Vue.use(DatetimePicker)

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')
