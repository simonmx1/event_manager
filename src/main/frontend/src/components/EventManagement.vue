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


          <v-dialog v-model="createDialog" persistent max-width="500px">
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
              <event-form
                  ref="eventForm"
                  class="pa-5"
                  @confirm="confirmCreate"/>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="closeCreateDialog()" color="primary">Cancel</v-btn>
                <v-btn @click="tryCreate()" color="green">Create</v-btn>
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
            <edit-event v-if="currentEvent" @close="editDialog = false" :user="currentEvent"/>
          </v-dialog>
        </v-toolbar>
      </template>

      <template v-slot:item.participants="{ item }">
        <v-menu
            transition="slide-y-transition"
            bottom
            offset-y
        >
          <template v-if="item.participants.length > 0" v-slot:activator="{ on, attrs }">
            <v-btn
                dark
                v-bind="attrs"
                v-on="on"
                icon
            >
              <v-icon>
                mdi-account-multiple
              </v-icon>
            </v-btn>
          </template>
          <v-list>
            <v-list-item
                v-for="(participants,id) in item.participants"
                :key="id"
            >
              <v-chip
                  color="#437505">
                {{ participants.email }}
              </v-chip>
              <v-chip
                  style="margin-left: 5px"
                  color="#054375">
                {{ participants.username }}
              </v-chip>
            </v-list-item>
          </v-list>
        </v-menu>
      </template>
      <template v-slot:item.location="{ item }">
        <span v-if="item.location">
              {{item.location.name}}
          <template>
            <location-info-dialog :current-location="item.location"></location-info-dialog>
          </template>
        </span>
      </template>
      <template v-slot:item.timeslot="{ item }">
        <span v-if="item.timeslot">
              {{formatTimeSlot(item.timeslot)}}
        </span>
      </template>
      <template v-slot:item.evaluated="{ item }">
        <v-checkbox disabled v-model="item.evaluated"/>
      </template>
      <template v-slot:item.createDate="{ item }">
        {{ formatDate(item.createDate) }}
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
import LocationInfoDialog from "@/components/LocationInfoDialog";

export default {
  name: "EventManagement",
  components: {LocationInfoDialog, EventForm},
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
      {text: 'Poll ended', align: 'left', value: 'evaluated'},
      {text: 'Created', align: 'left', value: 'createDate'},
      {text: 'Actions', value: 'actions'},
    ],
  }),
  methods: {
    formatDate(date) {
      return new Date(date).toISOString().slice(0, 10);
    },
    formatTimeSlot(timeslot){
      return this.formatTimeStamp(timeslot.start) + " - "  + this.formatTimeStamp(timeslot.end)
    },
    formatTimeStamp(timestamp){
      //2023-01-02T19:15:00.000+00:00
      const date = new Date(timestamp).toISOString().slice(0, 10)
      const time = new Date(timestamp).toISOString().slice(11, 16)
      return time + " " + date
    },
    closeCreateDialog() {
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
      api.event.getAll().then(response => this.events = response).then(() => console.log(this.events))
    },
    tryCreate(){
      this.$refs.eventForm.sendData();
    },
    confirmCreate(event) {
      event.participants.forEach((user, index) => event.participants[index] = user.username)
      event.location.forEach((location, index) => event.location[index] = location.id)
      api.user.loggedIn().then(response => {
        event.creatorUsername = response[0]
      }).then(() => api.event.create(event))
    }
  },
  mounted() {
    this.getEvents();
  }
}
</script>

<style scoped>

</style>