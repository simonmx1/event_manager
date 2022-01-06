<template>
  <v-app app>
    <v-app-bar app clipped-left>
      <v-app-bar-nav-icon v-if="sessionActive" @click="drawer = !drawer"/>
      <v-img src="favicon.png" max-height="50" max-width="50" @click="navToHome()" style="cursor: pointer"/>
      <div @click="navToHome()" style="cursor: pointer">
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
            <v-list-item-title @click="logout()">
              <v-icon>mdi-cog</v-icon>
              Settings
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
    <v-navigation-drawer app clipped v-model="drawer">
      <v-list>
        <v-list-item>
          <v-btn @click="closeDrawer()" to="/users">User Management</v-btn>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-container @click="closeDrawer()" style="height: 100%">
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
    users: null,
    sessionActive: true,
    session: "Not logged in!",
  }),
  methods: {
    closeDrawer() {
      this.drawer = false;
    },
    logout() {
      api.logout().then(() => this.$router.push("/login"))
      this.closeDrawer()
    },
    setLoggedInLabel() {
      let text = "";
      api.loggedIn().then(result => {
        text = result.toString();
        this.session = text;
        this.sessionActive = result !== false;
      });
    },
    navToHome() {
      (this.$route.path !== "/home") ? this.$router.push("/home") : null
    }
  },
  computed: {},
  mounted() {
    this.setLoggedInLabel();
    //console.log(this.sessionActive)

    if (!this.sessionActive && this.$route.path !== "/login") {
      this.$router.push("/login")
    }
  },
  watch: {
    $route: function () {
      this.setLoggedInLabel();
    },
    sessionActive: function() {
      //console.log(val)
    }
  },
}
</script>
