<template>
  <v-row class="pa-4">
    <v-col cols="6">
      <v-card-title>
        Order the locations to your preference
      </v-card-title>
      <v-progress-linear v-if="!locations"
                         indeterminate
                         color="primary"
      ></v-progress-linear>
      <draggable v-model="locations" group="people" @start="drag=true" @end="drag=false">
        <div v-for="(item, index) in locations"
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
        Order the timeslots to your preference
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
                    <v-col cols="7">
                      <div style="float: right">
                        {{ formatTimeStamp(item.timeslot.start).date }} at {{
                          formatTimeStamp(item.timeslot.start).time
                        }}
                      </div>
                      <br>
                      <div style="float: right">
                        {{ formatTimeStamp(item.timeslot.end).date }} at {{
                          formatTimeStamp(item.timeslot.end).time
                        }}
                      </div>
                    </v-col>
                    <v-col cols="1">
                      <v-btn icon style="height: 100%; width: 43px; padding: 5px "
                             @click="disableTimeslot(item, index)">
                        <v-icon color="red">mdi-close</v-icon>
                      </v-btn>
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
      <v-item-group>
        <div v-for="(item, index) in disabledTimeslots"
             :key="item.timeslot.id"
        >
          <tr>
            <th style="vertical-align: middle">
              <v-chip color="red">X</v-chip>
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
                    <v-col cols="7">
                      <div style="float: right">
                        {{ formatTimeStamp(item.timeslot.start).date }} at
                        {{ formatTimeStamp(item.timeslot.start).time }}
                      </div>
                      <br>
                      <div style="float: right">
                        {{ formatTimeStamp(item.timeslot.end).date }} at
                        {{ formatTimeStamp(item.timeslot.end).time }}
                      </div>
                    </v-col>
                    <v-col cols="1">
                      <v-btn icon style="height: 100%; width: 43px; padding: 5px "
                             @click="enableTimeslot(item, index++)">
                        <v-icon color="green">mdi-check</v-icon>
                      </v-btn>
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
      </v-item-group>
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
    timeslots: null,
    disabledTimeslots: [],
    monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    weekdayNames: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
  }),
  methods: {
    disableTimeslot(timeslot, index) {
      this.timeslots.splice(index, 1)
      this.disabledTimeslots.push(timeslot)
    },
    enableTimeslot(timeslot, index) {
      this.disabledTimeslots.splice(index, 1)
      this.timeslots.push(timeslot)
    },
    formatTimeStamp(timestamp) {
      const time = new Date(timestamp).toTimeString().slice(0, 8)
      return {"date": this.formatDate(new Date(timestamp)), "time": time}
    },
    formatDate(date) {
      const weekday = this.weekdayNames[date.getDay()]
      const day = date.getDate();
      const month = this.monthNames[date.getMonth()]
      const year = date.getFullYear();
      return weekday + ", " + day + " " + month + " " + year;
    },
    getPoll() {
      api.user.loggedIn()
          .then(response => api.poll.get(this.event.id, response[0])
              .then(response => this.poll = response)
          )
          .then(() => {
            this.locations = this.poll.pollLocations.sort((a, b) => b.points - a.points)
            this.timeslots = this.poll.pollTimeslots.sort((a, b) => b.points - a.points).filter(t => t.points !== 0)
            this.poll.pollTimeslots.forEach((item) => {
              if (item.points === 0) {
                this.disabledTimeslots.push(item)
              }
            })
          })
    },
    sendData() {
      console.log(this.disabledTimeslots);
      this.$emit("confirm", {
        locations: this.locations,
        timeslots: this.timeslots,
        poll: this.poll,
        disabledTimeslots: this.disabledTimeslots
      })
    },
  },
  mounted() {
    this.getPoll()
  }
}
</script>

<style scoped>

</style>