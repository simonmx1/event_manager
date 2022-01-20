<template>
  <v-row>
    <v-col cols="6">
      <v-card-title>
        Sort your Locations:
      </v-card-title>
      <v-progress-linear v-if="!locations"
          indeterminate
          color="primary"
      ></v-progress-linear>
      <draggable v-model="locations" group="people" @start="drag=true" @end="drag=false">
        <div v-for="(item, index) in locations "
             :key="item.location.id">
          <tr>
            <th>
              <v-chip>{{ index + 1 }}</v-chip>
            </th>
            <th style="width: 100%">
              <v-card color="#3d3d3d" class="pa-2, ma-2" style="height: 75px">
                <v-card-title>
                  <div style="margin-right: 10px; margin-top: 5px">
                    <template>
                      <location-info-dialog :current-location="item.location"></location-info-dialog>
                    </template>
                  </div>
                  <div style="margin-top: 5px">
                    {{ item.location.name }}
                  </div>
                  <v-spacer></v-spacer>
                  <v-icon style="margin-top: 5px" color="#aaa">mdi-reorder-horizontal</v-icon>
                </v-card-title>
              </v-card>
            </th>
          </tr>
        </div>
      </draggable>
    </v-col>
    <v-divider vertical></v-divider>
    <v-col cols="6">
      <v-card-title>
        Sort your Timeslots:
      </v-card-title>
      <v-progress-linear v-if="!timeslots"
                         indeterminate
                         color="primary"
      ></v-progress-linear>
      <draggable v-model="timeslots" group="people" @start="drag=true" @end="drag=false">
        <div v-for="(item, index) in timeslots"
             :key="item.timeslot.id"
        >
          <tr>
            <th style="vertical-align: middle">
              <v-chip>{{ index + 1 }}</v-chip>
            </th>
            <th style="width: 100%">
              <v-card color="#3d3d3d" class="pa-2, ma-2" style="height: 75px">
                <v-card-subtitle style="font-size: medium; color: white">
                  <v-row>
                    <v-col cols="2">
                      <div style="float: left">
                        From:
                      </div>
                      <br>
                      <div style="float: left">
                        To:
                      </div>
                    </v-col>
                    <v-col cols="8">
                      <div style="float: right">
                        {{ formatTimeStamp(item.timeslot.start).date }} at {{ formatTimeStamp(item.timeslot.start).time }}
                      </div>
                      <br>
                      <div style="float: right">
                        {{ formatTimeStamp(item.timeslot.end).date }} at {{ formatTimeStamp(item.timeslot.end).time }}
                      </div>
                    </v-col>
                    <v-col cols="1">
                      <v-icon color="#aaa" style="height: 100%; width: 50px">mdi-reorder-horizontal</v-icon>
                    </v-col>
                  </v-row>
                </v-card-subtitle>
              </v-card>
            </th>
          </tr>
        </div>
      </draggable>
    </v-col>
  </v-row>
</template>

<script>
import draggable from 'vuedraggable'
import api from "../utils/api";
import LocationInfoDialog from "@/components/LocationInfoDialog";

export default {
  name: "PollForm",
  components: {
    draggable,
    LocationInfoDialog
  },
  props: {
    event: {type: Object, required: true}
  },
  data: () => ({
    drag: false,
    poll: null,
    locations: null,
    timeslots: null
  }),
  methods: {
    formatTimeStamp(timestamp) {
      const time = new Date(timestamp).toISOString().slice(11, 16)
      return {"date": this.formatDate(new Date(timestamp)), "time": time}
    },
    formatWeekday(weekday) {
      switch (weekday) {
        case 0:
          return 'Mon'
        case 1:
          return 'Tue'
        case 2:
          return 'Wed'
        case 3:
          return 'Thu'
        case 4:
          return 'Fri'
        case 5:
          return 'Sat'
        case 6:
          return 'Sun'
      }
    },
    formatMonth(month) {
      switch (month) {
        case 0:
          return 'Jan'
        case 1:
          return 'Feb'
        case 2:
          return 'Mar'
        case 3:
          return 'Apr'
        case 4:
          return 'May'
        case 5:
          return 'Jun'
        case 6:
          return 'Jul'
        case 7:
          return 'Aug'
        case 8:
          return 'Sep'
        case 9:
          return 'Oct'
        case 10:
          return 'Nov'
        case 11:
          return 'Dec'
      }
    },
    formatDate(date) {
      const weekday = this.formatWeekday(date.getDay());
      const day = date.getDate();
      const month = this.formatMonth(date.getMonth());
      const year = date.getFullYear();
      return weekday + ", " + day + " " + month + " " + year;
    },
    getPoll() {
      api.user.loggedIn()
          .then(response => api.poll.get(this.event.id, response[0])
              .then(response => this.poll = response)
          )
          .then(() => (
              this.locations = this.poll.pollLocations,
                  this.timeslots = this.poll.pollTimeslots))
    }
  },
  mounted() {
    this.getPoll()
  }
}
</script>

<style scoped>

</style>