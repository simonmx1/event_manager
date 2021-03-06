<template>
  <v-container fluid>
    <v-data-iterator
        :loading="!loadedEvents"
        :items="items"
        :search="search"
        sort-by="pollEndDate"
        :sort-desc=true
        hide-default-footer
        disable-pagination
    >
      <template v-slot:header>
        <v-toolbar
            dark
            color="blue darken-3"
            class="pa-1"
            style="margin-bottom: 20px"
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
                      <v-btn @click="confirmPoll(index)" color="primary">Save</v-btn>
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
                    {{ $date.formatTimestampPoll(item.timeslot.start).date }} at {{ $date.formatTimestampPoll(item.timeslot.start).time }}
                  </div>
                  <br>
                  To:
                  <div style="float: right">
                    {{ $date.formatTimestampPoll(item.timeslot.end).date }} at {{ $date.formatTimestampPoll(item.timeslot.end).time }}
                  </div>
                </v-card-subtitle>
              </div>
              <div v-else>
                <v-progress-linear style="margin-top: 15px"
                                   :color="$date.calculateColor($date.calculatePercent(item.pollEndDate))"
                                   rounded
                                   :value="$date.calculatePercent(item.pollEndDate)"
                ></v-progress-linear>
                <v-card-subtitle style="font-size: medium">
                  <b>Poll end time</b>
                  <div style="float: right">
                    {{ $date.formatTimestampPoll(item.pollEndDate).date }} at {{ $date.formatTimestampPoll(item.pollEndDate).time }}
                  </div>
                </v-card-subtitle>
              </div>
              <div>
                <v-card-subtitle style="font-size: medium">
                  <b>Participants</b>

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
              <div>
                <v-card-subtitle style="font-size: medium" v-if="item.evaluated">
                  <b>Poll Result</b>
                  <span style="float: right">
                   <poll-result-dialog :current-event="item" style="float: right"></poll-result-dialog>
                  </span>
                </v-card-subtitle>
              </div>
              <div v-if="isCreator(item.creator)">
                <v-divider style="margin-top: 10px;"/>
                <v-card-actions class="ma-1">
                  <v-btn text color="orange" @click="evaluateEvent(item)" v-if="!item.evaluated">Evaluate Poll</v-btn>
                  <v-spacer/>
                  <v-btn icon small @click="deleteDialog = true, currentEvent = item">
                    <v-icon small>mdi-delete</v-icon>
                  </v-btn>
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
                </v-card-actions>
              </div>
            </v-card>
          </v-col>
        </v-row>
      </template>
    </v-data-iterator>
  </v-container>
</template>

<script>
import LocationInfoDialog from "./LocationInfoDialog";
import PollInfoDialog from "./PollInfoDialog"
import PollForm from "./PollForm";
import EventForm from "@/components/EventForm";
import PollResultDialog from "@/components/PollResultDialog";

export default {
  name: 'EventOverview',
  components: {PollResultDialog, EventForm, PollForm, LocationInfoDialog, PollInfoDialog},
  data() {
    return {
      currentUsername: null,
      success: undefined,
      response: null,
      createDialog: false,
      search: '',
      filter: {},
      sortDesc: false,
      pollDialog: [],
      currentEvent: null,
      loadedEvents: false,
      items: [],
      deleteDialog: false
    }
  },
  methods: {
    isCreator(creator) {
      return creator.username === this.currentUsername
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
      this.$api.user.loggedIn().then(response => {
        this.$api.event.getAllFromUser(response[0])
            .then(response => this.items = response)
            .then(() => this.items.forEach(() => this.pollDialog.push(false)))
            .then(() => this.loadedEvents = true)
      })
    },
    savePoll(event) {
      const arrayLocations = this.getPollswithPoints(event.locations)
      arrayLocations.forEach(pollLocation => this.$api.pollLocations.edit(pollLocation, event.poll))
      const arrayTimeslots = this.getPollswithPoints(event.timeslots)
      arrayTimeslots.forEach(pollTimeslot => this.$api.pollTimeslots.edit(pollTimeslot, event.poll))
      event.disabledTimeslots.forEach(disabled => {
        disabled.points = 0;
        this.$api.pollTimeslots.edit(disabled, event.poll)
      })
    },
    confirmPoll(index) {
      this.$refs.pollForm[0].sendData()
      this.closePollDialog(index)
    },
    getPollswithPoints(array) {
      let len = array.length
      for (let i = 0; i < array.length; i++) {
        array[i].points = len--
      }
      return array
    },
    evaluateEvent(event) {
      this.$api.event.evaluatePolls(event.id).then(() => this.getEvents())
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
      event.participants.forEach((user, index) => event.participants[index] = user.username)
      event.locations.forEach((location, index) => event.locations[index] = location.id)
      this.$api.user.loggedIn().then(response => {
        event.creatorUsername = response[0]
      }).then(() => this.$api.event.create(event).then(response => {
        this.success = response.status === 201;
        this.response = response.data
        if (this.success)
          this.$refs.eventForm.clear()
        else
          this.$refs.eventForm.resetConfirmedData()
      }))
    },
    deleteEventConfirm() {
      this.$api.event.delete(this.currentEvent.id).then(() => this.getEvents())
      this.currentEvent = null;
      this.deleteDialog = false
    },
  },
  computed: {
    filteredKeys() {
      return this.keys.filter(key => key !== 'Name')
    },
  },
  mounted() {
    this.$api.user.loggedIn().then(response => {
      this.currentUsername = response[0]
    })
    this.getEvents()
  }
}
</script>