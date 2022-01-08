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

        </v-toolbar>
      </template>
      <template v-slot:item.tags="{ item }">
        <v-combobox
            v-model="item.tags"
            multiple
            chips
            hide-selected
        ></v-combobox>
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
    name: "LocationManagement",
    components: {
    },
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
      locations: [],
      select: [],
    }),
    methods: {
      formatTags() {
        console.log(this.item)
      },
      openEditDialog(location){
        console.log(location)
      },
      openDeleteDialog(location){
        console.log(location)
      },
      getLocations() {
        console.log(api.getLocations().then(response => this.locations = response));
      }
    },
    mounted() {
      this.getLocations()
    }
  }
</script>

<style scoped>

</style>