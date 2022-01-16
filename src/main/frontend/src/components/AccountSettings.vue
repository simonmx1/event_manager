<template>
  <v-main>
    <v-card class="pa-5">
      <v-card-title>Account Details</v-card-title>
      <user-form v-if="user != null" :user="user"></user-form>
    </v-card>
  </v-main>
</template>

<script>
import UserForm from "@/components/UserForm";
import api from "@/utils/api";

export default {
  name: "AccountSettings",
  components: {UserForm},
  data: () => ({
    user: null
  }),
  mounted() {
    api.user.loggedIn2().then(response => {
      if (response !== false)
        api.user.get(response[0]).then(user => {
              if (user !== false)
                this.user = user
            }
        )

    })
  }
}
</script>

<style scoped>

</style>