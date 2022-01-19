<template>
  <v-container fluid>
    <v-data-iterator
        :loading="items.length === 0"
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
            <v-btn-toggle
                v-model="sortDesc"
                mandatory
            >
              <v-btn
                  large
                  depressed
                  color="blue"
                  :value="false"
              >
                <v-icon>mdi-arrow-up</v-icon>
              </v-btn>
              <v-btn
                  large
                  depressed
                  color="blue"
                  :value="true"
              >
                <v-icon>mdi-arrow-down</v-icon>
              </v-btn>
            </v-btn-toggle>
          </template>
        </v-toolbar>
      </template>

      <template v-slot:default="props">
        <v-row>
          <v-col
              v-for="(item, index) in props.items"
              :key="item.name"
              cols="4"
          >
            <v-card class="pa-3">
              <v-card-title class="subheading font-weight-bold">
                {{ item.name }}
              </v-card-title>

              <v-divider></v-divider>

              <div v-if="item.evaluated">
                <v-card-subtitle style="font-size: medium">
                  <b>Location: </b>
                  <span style="float: right">
                  <v-chip color="primary">{{ item.location.name }}</v-chip>
                  <location-info-dialog :current-location="item.location"></location-info-dialog>
                  </span>
                </v-card-subtitle>
                <v-card-subtitle style="font-size: medium">
                  <b>Timeslot: </b>
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
                <v-btn @click="showPollDialog(index, item)">
                  Vote
                </v-btn>
                <v-dialog v-model="pollDialog[index]">
                  <v-card>
                    <v-toolbar>
                      <v-card-title>Choose your poll options</v-card-title>
                    </v-toolbar>
                    <poll-form v-if="currentEvent != null && pollDialog[index]" :event="currentEvent"></poll-form>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn @click="closePollDialog(index)" color="primary">Cancel</v-btn>
                      <v-btn @click="closePollDialog(index)" color="red">Save</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
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
import PollForm from "./PollForm";

export default {
  name: 'EventOverview',
  components: {PollForm, LocationInfoDialog},
  data() {
    return {
      search: '',
      filter: {},
      sortDesc: false,
      sortBy: 'name',
      pollDialog: [],
      currentEvent: null,
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
    formatTimeStamp(timestamp) {
      const date = new Date(timestamp).toISOString().slice(0, 10)
      const time = new Date(timestamp).toISOString().slice(11, 16)
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
      api.event.getAll()
          .then(response => this.items = response)
          .then(() => this.items.forEach(() => this.pollDialog.push(false)))
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