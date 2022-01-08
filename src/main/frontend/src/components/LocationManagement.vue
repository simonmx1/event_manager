<template>
  <v-main>
    <v-data-table
        :loading="locations === []"
        :headers="headers"
        :items="locations"
        :items-per-page="15"
        :search="search"
        class="elevation-1">

      <template v-slot:top>
        <v-toolbar
            flat
        >
          <v-toolbar-title>Locations</v-toolbar-title>
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
          <v-dialog>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  v-bind="attrs"
                  v-on="on"
              >
                Create Location
              </v-btn>
            </template>
            <register :admin="true" @close="userCreated()"/>
          </v-dialog>
        </v-toolbar>
        <v-dialog v-model="deleteDialog" max-width="500px">
          <v-card>
            <v-card-title style="width: 100%">Are you sure you want to delete this user?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn @click="deleteDialog = false; currentLocation = null" color="primary">Cancel</v-btn>
              <v-btn @click="deleteLocationConfirm()" color="red">Delete</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
      <template v-slot:item.menu="{ item }">
        <a :href="'//' + item.menu" target="_blank">
          {{ item.menu }}
        </a>
      </template>
      <template v-slot:item.tags="{ item }">
        <v-item-group>
          <v-item
              v-for="(tag,id) in item.tags"
              :key="id"
          >
            <v-chip
                style="margin-right: 5px"
                :color="'#437505'">
              {{ tag.tag }}
            </v-chip>
          </v-item>
        </v-item-group>
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
        >
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-main>
</template>

<script>

import api from "@/utils/api";

export default {
  currentLocation: null,
  name: "LocationManagement",
  components: {},
  data: () => ({
    createDialog: false,
    deleteDialog: false,
    editDialog: false,
    currentLocation: null,
    search: '',
    headers: [
      {text: 'Name', align: 'left', value: 'name'},
      {text: 'Menu', align: 'left', value: 'menu'},
      {text: 'Position', align: 'left', value: 'geolocation'},
      {text: 'Tags', align: 'left', value: 'tags'},
      {text: 'Enabled', align: 'left', value: 'enabled'},
      {text: 'Actions', value: 'actions'},
    ],
    locations: []
  }),
  methods: {
    openEditDialog(location) {
      console.log(location)
    },
    openDeleteDialog(location) {
      this.currentLocation = location;
      this.deleteDialog = true;
    },
    deleteLocationConfirm() {
      api.deleteLocation(this.currentLocation.locationId).then(() => this.getLocations())
      this.deleteDialog = false
    },
    getLocations() {
      api.getLocations().then(response => this.locations = response);
    }
  },
  mounted() {
    this.getLocations()
  }
}
</script>

<style scoped>

</style>