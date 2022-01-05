<template>
  <v-main>
    <v-data-table
        :loading="users === []"
        :headers="headers"
        :items="users"
        :items-per-page="15"
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
          <v-spacer></v-spacer>
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
            <register @close="userCreated()"/>
          </v-dialog>
        </v-toolbar>
      </template>

      <template v-slot:item.enabled="{ item }">
        <v-simple-checkbox
            v-model="item.enabled"
            disabled
        ></v-simple-checkbox>
      </template>
      <template v-slot:item.role="{ item }">
        <v-chip
            :color="getColor(item.role)"
            dark
        >
          {{ item.role }}
        </v-chip>
      </template>
      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="editUser(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="deleteUser(item)"
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

export default {
  name: "UserManagement",
  components: {
    Register
  },
  data: () => ({
    createDialog: false,
    headers: [
      {text: 'Username', align: 'left', value: 'username'},
      {text: 'Firstname', align: 'left', value: 'firstName'},
      {text: 'Lastname', align: 'left', value: 'lastName'},
      {text: 'Email', align: 'left', value: 'email'},
      {text: 'Role', align: 'left', value: 'role'},
      {text: 'Enabled', align: 'center', value: 'enabled'},
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
    userCreated() {
      this.getUsers()
      this.createDialog = false
    },
    editUser(user) {
      console.log(user)
    },
    deleteUser(user) {
      console.log(user)
    },
    getUsers() {
      api.getUsers().then(response => this.users = response)
    }
  },
  mounted() {
    this.getUsers()
  }
}
</script>

<style scoped>

</style>