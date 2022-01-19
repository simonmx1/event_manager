<template>
  <v-app app>
    <v-app-bar app clipped-left>
      <v-app-bar-nav-icon
          app
          v-if="sessionActive"
          @click="drawer = !drawer"/>
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
      <v-menu v-if="sessionActive" offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-chip
              color="primary"
              dark
              v-bind="attrs"
              v-on="on"
          >
            <v-icon>mdi-account</v-icon>
            {{ session }}
          </v-chip>
        </template>
        <v-list>
          <v-list-item>
            <v-list-item-title @click="logout()">
              <v-icon>mdi-logout</v-icon>
              Logout
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title @click="settings()">
              <v-icon>mdi-cog</v-icon>
              Settings
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
      <v-navigation-drawer app clipped temporary v-model="drawer">
      <v-list
          dense
          nav
      >
        <v-list-item
            v-for="item in navBarItems"
            :key="item.title"
            link
            @click="closeDrawer()"
            :to="item.url"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
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
