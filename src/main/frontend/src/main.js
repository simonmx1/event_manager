import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import DatetimePicker from 'vuetify-datetime-picker'
import api from "@/utils/api";
import date from "@/utils/date";

Vue.prototype.$api = api
Vue.prototype.$date = date
Vue.config.productionTip = false
Vue.use(DatetimePicker)

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')
