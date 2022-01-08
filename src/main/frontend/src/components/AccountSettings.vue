<template>
  <v-main>
    <v-card class="pa-5">
      <v-card-title>Account Details</v-card-title>
      <user-form
        v-if="user != null"
        :user="user"
        :accountSettings="true"
      ></user-form>
      <v-card-actions>
        <v-dialog v-model="editDialog" width="500" persistent>
          <template v-slot:activator="{ on, attrs }">
            <v-btn color="red" dark v-bind="attrs" v-on="on" @click="openEditDialog">
              Edit
            </v-btn>
          </template>
          <edit-user @close="closeEditDialog"
            :user="user"
            :admin="false"
          />
        </v-dialog>
        <v-spacer></v-spacer>
      </v-card-actions>
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
    editDialog: false,
  }),
  methods: {
    openEditDialog() {
      this.editDialog = true;
    },
    closeEditDialog() {
      this.editDialog = false;
      this.user = null;
      this.getUser()

    },
    getUser() {
      api.loggedIn().then((response) => {
      if (response !== false)
        api.getUser(response[0]).then((user) => {
          if (user !== false) this.user = user;
        });
      });
    }
  },
  mounted() {
    this.getUser()
  },
};
</script>

<style scoped>
</style>