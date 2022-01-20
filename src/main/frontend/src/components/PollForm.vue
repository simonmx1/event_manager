<template>
  <v-row>
    <v-col cols="6">
      <v-card-title>
        Sort your Locations:
      </v-card-title>
      <draggable v-model="locations" group="people" @start="drag=true" @end="drag=false">
        <div v-for="(item, index) in locations "
             :key="item.location.id">
          <tr>
            <th>
              <v-chip>{{ index + 1 }}</v-chip>
            </th>
            <th style="width: 100%">
              <v-card color="#3d3d3d" class="pa-2, ma-2">
                <v-card-title>
                  <div style="margin-right: 10px">
                    <template>
                      <location-info-dialog :current-location="item.location"></location-info-dialog>
                    </template>
                  </div>
                  {{ item.location.name }}
                  <v-spacer></v-spacer>
                  <v-icon color="#aaa">mdi-reorder-horizontal</v-icon>
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
      <draggable v-model="timeslots" group="people" @start="drag=true" @end="drag=false">
        <div v-for="(item, index) in timeslots"
             :key="item.timeslot.id"
        >

          <tr>
            <th>
              <v-chip>{{ index + 1 }}</v-chip>
            </th>
            <th style="width: 100%">
              <v-card color="#3d3d3d" class="pa-2, ma-2">
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