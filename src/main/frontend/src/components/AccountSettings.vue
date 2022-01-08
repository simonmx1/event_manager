<template>
  <v-main>
    <v-card class="pa-5">
      <v-card-title>Account Details</v-card-title>
      <v-card-actions>
        <v-dialog v-model="dialog" width="500" persistent>
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="red lighten-2" dark v-bind="attrs" v-on="on" @click="openEditDialog">
              Edit
            </v-btn>
          </template>
          <edit-user @close="dialog = false; currentUser = null; this.user"/>
        </v-dialog>
        <v-spacer></v-spacer>
      </v-card-actions>
      <user-form
        v-if="user != null"
        :user="user"
        :accountSettings="true"
      ></user-form>
    </v-card>
  </v-main>
</template>

<script>
import UserForm from "@/components/UserForm";
import EditUser from "@/components/EditUser"
import api from "@/utils/api";

export default {
  name: "AccountSettings",
  components: {
    UserForm,
    EditUser
  },
  data: () => ({
    user: null,
    dialog: false,
  }),
  methods: {
    openEditDialog() {
      this.editDialog = true;
    },
  },
  mounted() {
    api.loggedIn().then((response) => {
      if (response !== false)
        api.getUser(response[0]).then((user) => {
          if (user !== false) this.user = user;
        });
    });
  },
};
</script>

<style scoped>
</style>