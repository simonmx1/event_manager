<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
    >
      <v-app-bar-nav-icon/>
      <v-app-bar-title>EventManager</v-app-bar-title>
    </v-app-bar>
    <v-main>
      <login/>
    </v-main>
  </v-app>
</template>

<script>
import Login from './components/Login';

export default {
  name: 'App',

  components: {
    Login,
  },

  data: () => ({
    text: 'hello'
  }),
};
</script>
