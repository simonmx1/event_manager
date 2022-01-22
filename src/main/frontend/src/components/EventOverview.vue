<template>
  <v-container fluid>
    <v-data-iterator
        :loading="!loadedEvents"
        :items="items"
        :search="search"
        :sort-by="sortBy.toLowerCase()"
        :sort-desc="sortDesc"
        hide-default-footer
        disable-pagination
    >
      <template v-slot:header>
        <v-toolbar
            dark
            color="blue darken-3"
            class="mb-1"
        >
          <v-text-field
              v-model="search"
              clearable
              flat
              solo-inverted
              hide-details
              prepend-inner-icon="mdi-magnify"
              label="Search"
          ></v-text-field>
          <template v-if="$vuetify.breakpoint.mdAndUp">
            <v-spacer></v-spacer>
            <v-select
                v-model="sortBy"
                flat
                solo-inverted
                hide-details
                :items="keys"
                prepend-inner-icon="mdi-magnify"
                label="Filter by"
            ></v-select>
            <v-spacer></v-spacer>
            <v-dialog v-model="createDialog" persistent max-width="1000px">
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                    :color="'#910101'"
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
                <v-alert
                    v-if="typeof success !== 'undefined'"
                    dense
                    outlined
                    :type="success ? 'success' : 'error'"
                >
                  <strong>{{ response }}</strong>
                </v-alert>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn @click="closeCreateDialog()" color="primary">Cancel</v-btn>
                  <v-btn @click="tryCreate()" color="green">Create</v-btn>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </template>
        </v-toolbar>
      </template>

      <template v-slot:default="props">
        <v-row>
          <v-col
              v-for="(item, index) in props.items"
              :key="index"
              cols="4"
          >
            <v-card class="pa-3">
              <v-card-title class="subheading font-weight-bold">
                {{ item.name }}
                <v-spacer></v-spacer>
                <v-btn v-if="!item.evaluated" @click="showPollDialog(index, item)" color="green">
                  Vote
                </v-btn>
                <v-dialog v-model="pollDialog[index]" width="1000px" persistent>
                  <v-card style="overflow: hidden">
                    <v-toolbar>
                      <v-card-title>{{ item.name }}</v-card-title>
                      <v-spacer/>
                      <template>
                        <poll-info-dialog></poll-info-dialog>
                      </template>
                    </v-toolbar>
                    <poll-form ref='pollForm'
                               class="pa-5"
                               @confirm="savePoll"
                               v-if="currentEvent != null && pollDialog[index]" :event="currentEvent"/>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn @click="closePollDialog(index)" color="red">Cancel</v-btn>
                      <v-btn @click="confirmPoll()" color="primary">Save</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-card-title>

              <v-divider></v-divider>

              <div v-if="item.evaluated">
                <v-card-subtitle style="font-size: medium">
                  <b>Location</b>
                  <span style="float: right">
                  <v-chip color="primary">{{ item.location.name }}</v-chip>
                  <location-info-dialog :current-location="item.location"></location-info-dialog>
                  </span>
                </v-card-subtitle>
                <v-card-subtitle style="font-size: medium">
                  <b>Timeslot</b>
                  <br>
                  From:
                  <div style="float: right">
                    {{ formatTimeStamp(item.timeslot.start).date }} at {{ formatTimeStamp(item.timeslot.start).time }}
                  </div>
                  <br>
                  To:
                  <div style="float: right">
                    {{ formatTimeStamp(item.timeslot.end).date }} at {{ formatTimeStamp(item.timeslot.end).time }}
                  </div>
                </v-card-subtitle>
              </div>
              <div v-else>
                <v-progress-linear style="margin-top: 15px"
                                   :color="calculateColor(calculatePercent(item.pollEndDate))"
                                   rounded
                                   :value="calculatePercent(item.pollEndDate)"
                ></v-progress-linear>
                <v-card-subtitle style="font-size: medium">
                  <b>Poll end time</b>
                  <div style="float: right">
                    {{ formatTimeStamp(item.pollEndDate).date }} at {{ formatTimeStamp(item.pollEndDate).time }}
                  </div>
                </v-card-subtitle>
              </div>
              <div>
                <v-card-subtitle style="font-size: medium">
                  <b>Participants: </b>

                  <v-menu
                      transition="slide-y-transition"
                      bottom
                      offset-y
                  >
                    <template v-if="item.participants.length > 0" v-slot:activator="{ on, attrs }">
                      <v-btn
                          style="float: right"
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
                </v-card-subtitle>
              </div>
            </v-card>
          </v-col>
        </v-row>
      </template>

      <template v-slot:footer>
        <v-toolbar
            class="mt-2"
            color="primary"
            justify="center"
        >
          Footer
          <v-spacer></v-spacer>
        </v-toolbar>
      </template>
    </v-data-iterator>
  </v-container>
</template>

<script>
import api from "../utils/api";
import LocationInfoDialog from "./LocationInfoDialog";
import PollInfoDialog from "./PollInfoDialog"
import PollForm from "./PollForm";
import EventForm from "@/components/EventForm";

export default {
  name: 'EventOverview',
  components: {EventForm, PollForm, LocationInfoDialog, PollInfoDialog},
  data() {
    return {
      success: undefined,
      response: null,
      createDialog: false,
      search: '',
      filter: {},
      sortDesc: false,
      sortBy: 'name',
      pollDialog: [],
      currentEvent: null,
      loadedEvents: false,
      keys: [
        'Name',
      ],
      items: [],
    }
  },
  methods: {
    /*formatTimeSlot(timeslot){
      return this.formatTimeStamp(timeslot.start) + " - "  + this.formatTimeStamp(timeslot.end)
    },*/
    calculatePercent(timestamp) {
      let dif = new Date(timestamp).getTime() - new Date().getTime()
      let p = 100 - dif / 1000 / 3600 / 24 * 100
      return dif < 86400000 ? p : dif < 0 ? 100 : 0
    },
    calculateColor(percent) {
      if (percent >= 95.83) {//1 hour
        return 'red'
      } else if (percent >= 91.66) {//2 hours
        return 'orange'
      } else if (percent >= 87.5) {//3 hours
        return '#e6c000'
      } else {
        return 'green'
      }
    },
    formatTimeStamp(timestamp) {
      const date = new Date(timestamp).toISOString().slice(0, 10)
      const time = new Date(timestamp).toTimeString().slice(0, 8)
      return {"date": date, "time": time}
    },
    showPollDialog(index, item) {
      this.currentEvent = item
      this.pollDialog[index] = true
    },
    closePollDialog(index) {
      this.pollDialog[index] = false
      this.currentEvent = null
    },
    getEvents() {
      api.user.loggedIn().then(response => {
        api.event.getAllFromUser(response[0])
            .then(response => this.items = response)
            .then(() => this.items.forEach(() => this.pollDialog.push(false)))
            .then(() => this.loadedEvents = true)
      })
    },
    savePoll(event) {
      const arrayLocations = this.getPollswithPoints(event.locations)
      arrayLocations.forEach(pollLocation => api.pollLocations.edit(pollLocation, event.poll))
      const arrayTimeslots = this.getPollswithPoints(event.timeslots)
      arrayTimeslots.forEach(pollTimeslot => api.pollTimeslots.edit(pollTimeslot, event.poll))
      event.disabledTimeslots.forEach(disabled => {
        disabled.points = 0;
        api.pollTimeslots.edit(disabled, event.poll)
      })
    },
    confirmPoll() {
      this.$refs.pollForm[0].sendData()
    },
    getPollswithPoints(array) {
      let len = array.length
      for (let i = 0; i < array.length; i++) {
        array[i].points = len--
      }
      return array
    },
    closeCreateDialog() {
      this.getEvents()
      this.createDialog = false
      this.$refs.eventForm.clear()
      this.success = undefined
      this.response = null
    },
    tryCreate() {
      this.$refs.eventForm.sendData();
    },
    confirmCreate(event) {
      console.log(event.participants)
      event.participants.forEach((user, index) => event.participants[index] = user.username)
      event.location.forEach((location, index) => event.location[index] = location.id)
      api.user.loggedIn().then(response => {
        event.creatorUsername = response[0]
      }).then(() => api.event.create(event).then(response => {
        this.success = response.status === 201;
        this.response = response.data
      })).then(() => this.$refs.eventForm.clear())
    }
  },
  computed: {
    filteredKeys() {
      return this.keys.filter(key => key !== 'Name')
    },
  },
  mounted() {
    this.getEvents()
  }
}
</script>