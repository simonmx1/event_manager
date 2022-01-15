<template>
  <v-main>
    <v-data-table
        :loading="events === []"
        :headers="headers"
        :items="events"
        :items-per-page="15"
        :search="search"
        sort-by="role"
        sort-by.next="username"
        class="elevation-1">

      <template v-slot:top>
        <v-toolbar
            flat
        >
          <v-toolbar-title>Events</v-toolbar-title>
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


          <v-dialog v-model="createDialog">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  color="primary"
                  dark
                  class="mb-2"
                  v-bind="attrs"
                  v-on="on"
              >
                Create Event
              </v-btn>
            </template>
            <v-card>
              <v-toolbar color="primary">
                <v-card-title>
                Create Event
                </v-card-title>
              </v-toolbar>
              <event-form class="pa-5"/>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="createDialog = false" color="primary">Cancel</v-btn>
                <v-btn @click="deleteEventConfirm()" color="green">Create</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="deleteDialog" max-width="500px">
            <v-card>
              <v-card-title style="width: 100%">Are you sure you want to delete this Event?</v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="deleteDialog = false; currentEvent = null" color="primary">Cancel</v-btn>
                <v-btn @click="deleteEventConfirm()" color="red">Delete</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="editDialog" max-width="500px">
            <edit-user v-if="currentEvent" @close="editDialog = false" :user="currentEvent"/>
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
            class="mr-2"
            @click="openDeleteDialog(item)"
        >
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-main>
</template>

<script>
import EventForm from "@/components/EventForm";
import api from "@/utils/api";

export default {
  name: "EventManagement",
  components: {EventForm},
  data: () => ({
    createDialog: false,
    deleteDialog: false,
    editDialog: false,
    currentEvent: null,
    search: '',
    events: [],
    headers: [
      {text: 'Eventname', align: 'left', value: 'name'},
      {text: 'Participants', align: 'left', value: 'participants'},
      {text: 'Location', align: 'left', value: 'location'},
      {text: 'Timeslot', align: 'left', value: 'timeslot'},
      {text: 'Created', align: 'left', value: 'createDate'},
      {text: 'Actions', value: 'actions'},
    ],
  }),
  methods: {
    formatDate(date) {
      return new Date(date).toISOString().slice(0, 10);
    },
    userCreated() {
      this.getEvents()
      this.createDialog = false
    },
    openEditDialog(user) {
      this.currentEvent = user;
      this.editDialog = true;
    },
    openDeleteDialog(user) {
      this.currentEvent = user;
      this.deleteDialog = true;
    },
    deleteEventConfirm() {
      //api.deleteEvent(this.currentEvent.username).then(() => this.getEvents())
      this.currentEvent = null;
      this.deleteDialog = false
    },
    getEvents() {
      api.event.getAll().then(response => this.events = response)
    }
  },
  mounted() {
    this.getEvents();
  }
}
</script>

<style scoped>

</style>