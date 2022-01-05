<template>
  <v-app app>
    <v-app-bar app clipped-left>
      <v-app-bar-nav-icon v-if="hasKey()" @click="drawer = !drawer"/>
      <router-link to="/home">
        <v-img src="favicon.png" max-height="50" max-width="50"/>
      </router-link>
      <router-link to="/home">
        <v-app-bar-title style="color: #ffffff; margin-left: 10px">
          Event Manager
        </v-app-bar-title>
      </router-link>
      <v-spacer/>
      <v-btn v-if="hasKey()" color="primary" @click="logout()">
        <v-icon>mdi-logout</v-icon>
        Logout
      </v-btn>
    </v-app-bar>
    <v-navigation-drawer app clipped v-model="drawer">
      <v-list>
        <v-list-item>
          <v-btn to="/users">User Management</v-btn>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-container>
      <router-view/>
    </v-container>
  </v-app>
</template>

<script>

import api from "@/utils/api";

export default {
  name: 'App',
  data: () => ({
    drawer: false,
    users: null
  }),
  methods: {
    logout() {
      api.logout().then(this.$router.push("/login"))
    },
    navto() {
      console.log("hallo")
    },
    hasKey() {
      return JSON.parse(localStorage.getItem('jwt'))
    }
  },
  computed: {},
  mounted() {
    if (!localStorage.getItem('jwt') && this.$route.path !== "/login") {
      this.$router.push("/login")
    }
  }
}
</script>
