<template>
  <v-main>
    <v-card class="pa-5">
      <v-card-title>Account Details</v-card-title>
      <user-form
          v-if="user != null"
          :user="user"
          :account-settings="true"
          :edit="true"
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
        <v-dialog v-model="changePasswordDialog" width="500" persistent>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                style="margin-left: 10px"
                color="orange"
                v-bind="attrs"
                v-on="on"
                @click="changePasswordDialog = true"
            >Change Password
            </v-btn>
          </template>
          <change-password
              v-if="user != null"
              @close="changePasswordDialog = false"
              :username="user.username"
          />
        </v-dialog>

        <v-spacer></v-spacer>
        <v-dialog v-model="deleteDialog" max-width="500px">
          <v-card>
            <v-card-title style="width: 100%">Are you sure you want to delete your account?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn @click="deleteDialog = false" color="primary">Cancel</v-btn>
              <v-btn @click="deleteUserConfirm()" color="red">Delete</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-spacer></v-spacer>
        <v-btn color="error" @click="deleteDialog = true">Delete Account</v-btn>
      </v-card-actions>
    </v-card>
  </v-main>
</template>

<script>
import UserForm from "@/components/UserForm";
import EditUser from "@/components/EditUser"
import ChangePassword from "@/components/ChangePassword";

export default {
  name: "AccountSettings",
  components: {
    ChangePassword,
    UserForm,
    EditUser
  },
  data: () => ({
    user: null,
    editDialog: false,
    deleteDialog: false,
    changePasswordDialog: false
  }),
  methods: {
    openEditDialog() {
      this.editDialog = true;
    },
    closeEditDialog() {
      this.editDialog = false;
      this.getUser()

    },
    deleteUserConfirm() {
      this.deleteDialog = false
      this.$api.user.delete(this.user.username).then(response => {
        if (response !== false) {
          localStorage.removeItem('jwt')
          this.$router.push("/login")
        }
      })
    },
    getUser() {
      this.$api.user.loggedIn().then((response) => {
        if (response !== false)
          this.$api.user.get(response[0]).then((user) => {
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