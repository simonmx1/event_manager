<template>
  <v-row>
    <v-col cols="6">
      <v-card-title>
        Sort your Locations:
      </v-card-title>
      <draggable v-model="locations" group="people" @start="drag=true" @end="drag=false">
        <div v-for="(item) in locations "
             :key="item.location.id">
          <v-card color="#3d3d3d" class="pa-2, ma-2">
            <v-card-title>
              {{ item.location.name }}
              <v-spacer></v-spacer>
              <template style="float: right">
                <location-info-dialog :current-location="item.location"></location-info-dialog>
              </template>
            </v-card-title>
          </v-card>
        </div>
      </draggable>
    </v-col>
    <v-divider vertical></v-divider>
    <v-col cols="6">
      <v-card-title>
        Sort your Timeslots:
      </v-card-title>
      <draggable v-model="timeslots" group="people" @start="drag=true" @end="drag=false">
        <div v-for="item in timeslots"
             :key="item.timeslot.id"
        >
          <v-card color="#3d3d3d" class="pa-2, ma-2">
            <v-card-subtitle style="font-size: medium; color: white">
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
          </v-card>
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
    locations: [],
    timeslots: []
  }),
  methods: {
    formatTimeStamp(timestamp) {
      const date = new Date(timestamp).toISOString().slice(0, 10)
      const time = new Date(timestamp).toISOString().slice(11, 16)
      return {"date": date, "time": time}
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