<template>
  <v-app app style="background: #333">
    <v-app-bar app clipped-left>
      <v-img
          src="favicon.png"
          max-height="50"
          max-width="50"
          @click="navToHome()"
          style="cursor: pointer"/>
      <div @click="navToHome()"
           style="cursor: pointer">
        <v-app-bar-title style="color: #ffffff; margin-left: 10px">
          Event Manager
        </v-app-bar-title>
      </div>
      <v-spacer/>
      <v-list
          v-for="item in navBarItems"
          :key="item.title"
          link
          @click="closeDrawer()"
          :to="item.url"
      color="#0000">
        <v-list-item
            :to="item.url">
          {{ item.title }}
          <v-icon style="margin-left: 5px;">{{ item.icon }}</v-icon>
        </v-list-item>
      </v-list>
      <template v-if="sessionActive">
        <v-divider vertical style="margin: 0 20px 0 10px"/>
        {{ session }}
        <v-btn icon @click="settings()">
          <v-icon>mdi-cog</v-icon>
        </v-btn>
      </template>
      <template v-if="sessionActive" style="margin: 5px">
        <v-divider vertical style="margin: 0 10px 0 10px"/>
        <v-btn icon @click="logout()">
          <v-icon>mdi-export</v-icon>
        </v-btn>
      </template>
    </v-app-bar>
    <v-container style="height: 100%">
      <router-view/>
    </v-container>
  </v-app>
</template>

<script>

import api from "@/utils/api";
import permissions from "@/utils/permissions";

export default {
  name: 'App',
  data: () => ({
    drawer: false,
    sessionActive: true,
    session: "Not logged in!",
    navBarItems: []
  }),
  methods: {
    closeDrawer() {
      this.drawer = false;
    },
    logout() {
      api.user.logout().then(() => this.$router.push("/login"))
      this.closeDrawer()
    },
    settings() {
      this.$router.push("/accountSettings")
    },
    setLoggedInLabel() {
      api.user.loggedIn().then(result => {
        this.session = result.toString();
        this.sessionActive = result !== false;
      });
      permissions.getNavBarItems().then(response => this.navBarItems = response)
    },
    navToHome() {
      (this.$route.path !== "/") ? this.$router.push("/") : null
    }
  },
  computed: {},
  mounted() {
    this.setLoggedInLabel();
    if (!this.sessionActive && this.$route.path !== "/login") {
      this.$router.push("/login")
    }
  },
  watch: {
    $route: function () {
      this.setLoggedInLabel();
    },
  },
}
</script>
