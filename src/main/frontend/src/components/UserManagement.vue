<template>
  <v-main>
    <v-data-table
        :loading="users === []"
        :headers="headers"
        :items="users"
        :items-per-page="15"
        :search="search"
        sort-by="role"
        sort-by.next="username"
        class="elevation-1">

      <template v-slot:top>
        <v-toolbar
            flat
        >
          <v-toolbar-title>Users</v-toolbar-title>
          <v-divider
              class="mx-4"
              inset
              vertical
          ></v-divider>
          <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="Search"
              style="width: 50px"
              single-line
              hide-details
          ></v-text-field>
          <v-spacer/>
          <v-dialog
              v-model="createDialog"
              width="500"
              persistent
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  v-bind="attrs"
                  v-on="on"
              >
                Create User
              </v-btn>
            </template>
            <register :admin="true" @close="userCreated()"/>
          </v-dialog>
          <v-dialog v-model="deleteDialog" max-width="500px">
            <v-card >
              <v-card-title style="width: 100%">Are you sure you want to delete this user?</v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="deleteDialog = false; currentUser = null" color="primary">Cancel</v-btn>
                <v-btn @click="deleteUserConfirm()" color="red">Delete</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="editDialog" max-width="500px">
            <edit-user
                v-if="currentUser != null"
                @close="editDialog = false; currentUser = null; getUsers()"
                :user="currentUser"
                :admin="loggedInUser !== currentUser.username"></edit-user>
          </v-dialog>
        </v-toolbar>
      </template>

      <template v-slot:item.role="{ item }">
        <v-chip
            :color="getColor(item.role)"
            dark
        >
          {{ item.role }}
        </v-chip>
      </template>
      <template v-slot:item.createDate="{ item }">
        {{ formatDate(item.createDate) }}
      </template>
      <template v-slot:item.enabled="{ item }">
        <v-simple-checkbox
            v-model="item.enabled"
            disabled
        ></v-simple-checkbox>
      </template>
      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="openEditDialog(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="openDeleteDialog(item)"
            :disabled="loggedInUser === item.username"
        >
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-main>
</template>

<script>
import api from "@/utils/api";
import Register from "@/components/Register";
import EditUser from "@/components/EditUser";

export default {
  name: "UserManagement",
  components: {
    Register,
    EditUser
  },
  data: () => ({
    loggedInUser: null,
    createDialog: false,
    deleteDialog: false,
    editDialog: false,
    currentUser: null,
    search: '',
    headers: [
      {text: 'Username', align: 'left', value: 'username'},
      {text: 'Firstname', align: 'left', value: 'firstName'},
      {text: 'Lastname', align: 'left', value: 'lastName'},
      {text: 'Email', align: 'left', value: 'email'},
      {text: 'Role', align: 'left', value: 'role'},
      {text: 'Enabled', align: 'left', value: 'enabled'},
      {text: 'Created', align: 'left', value: 'createDate'},
      {text: 'Actions', value: 'actions'},
    ],
    users: []
  }),
  methods: {
    getColor(role) {
      switch (role) {
        case 'ADMIN':
          return '#ff0000';
        case 'LOCATION_MANAGER':
          return '#ff8000';
        case 'USER':
          return '#359100';
      }
    },
    formatDate(date) {
      return new Date(date).toISOString().slice(0, 10);
    },
    userCreated() {
      this.getUsers()
      this.createDialog = false
    },
    openEditDialog(user) {
      this.currentUser = user;
      this.editDialog = true;
    },
    openDeleteDialog(user) {
      this.currentUser = user;
      this.deleteDialog = true;
    },
    deleteUserConfirm() {
      api.user.delete(this.currentUser.username).then(() => this.getUsers())
      this.currentUser = null;
      this.deleteDialog = false
    },
    getUsers() {
      api.user.getAll().then(response => this.users = response)
    }
  },
  mounted() {
    this.getUsers()
    api.user.loggedIn2().then(response => {if (response !== false) {this.loggedInUser = response[0]}})
  }
}
</script>

<style scoped>

</style>