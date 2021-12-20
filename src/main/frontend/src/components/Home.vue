<template>
  <v-main>
    <v-app-bar clipped-left>
      <v-app-bar-nav-icon @click="drawer = !drawer"/>
      <v-app-bar-title>
        Event Manager
      </v-app-bar-title>
    </v-app-bar>
    <v-navigation-drawer clipped>

    </v-navigation-drawer>
    <v-container>
      <user-management/>
    </v-container>
  </v-main>
</template>

<script>
import UserManagement from "@/components/UserManagement";
// import axios from "axios";
export default {
  name: "Home",
  components: {UserManagement},
  data: () => ({
    drawer: false,
    users: null
  }),
  mounted() {
    fetch("http://localhost:8080/api/userlist/getUsers").then(data => console.log(data)).then(console.log(this.users))
  }
}
</script>

<style scoped>

</style>