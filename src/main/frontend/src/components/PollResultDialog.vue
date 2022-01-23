<template>
  <v-dialog v-model="showDialog" max-width="1200px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          icon
          @click.stop.prevent="showDialog = true"
          v-bind="attrs"
          v-on="on"
      >
        <v-icon> mdi-chart-box</v-icon>
      </v-btn>
    </template>
    <v-card style="overflow: hidden;">
      <v-toolbar color="primary">
        <v-card-title>
          Poll Result
        </v-card-title>
      </v-toolbar>
      <v-row
          class="pa-4"
          justify="space-between"
      >
        <v-col cols="3">
          <v-list-item-group
              class="overflow-y-auto"
              v-model="selectedItem"
              color="primary"
          >
            <v-list-item
                v-for="(item, i) in currentEvent.participants"
                :key="i"
                selectable
            >
              <div>
                <v-list-item-title>{{ item.firstName }} {{ item.lastName }}</v-list-item-title>
                <v-list-item-subtitle>{{ item.username }}</v-list-item-subtitle>
              </div>
            </v-list-item>
          </v-list-item-group>
        </v-col>

        <v-divider vertical></v-divider>

        <v-col
            cols="9"
            class="overflow-y-auto text-center"
            style="min-height: 500px"
        >
          <v-scroll-y-transition mode="out-in">
            <div
                v-if="!selectedUser"
                style="align-self: center;"
            >
              Select a User
            </div>
            <v-card
                v-else
                :key="selectedUser.id"
                class="pt-6 mx-auto"
                flat
                max-width="100%"
            >
              <div>
                <h2>{{ selectedUser.firstName }} {{ selectedUser.lastName }}</h2>
                <v-card-subtitle style="margin-top: -10px">{{ selectedUser.username }}</v-card-subtitle>
              </div>
              <v-divider></v-divider>
              <v-row>
                <v-col cols="6">
                  <v-list>
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
                              </div>
                              <div style="margin-top: 5px">
                                {{ item.location.name }}
                              </div>
                            </v-card-title>
                          </v-card>
                        </th>
                      </tr>
                    </div>
                  </v-list>
                </v-col>
                <v-col cols="6">
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
                              <v-col cols="10">
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
                            </v-row>
                          </v-card-subtitle>
                        </v-card>
                      </th>
                    </tr>
                  </div>
                  <div v-for="(item) in disabledTimeslots"
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
                              <v-col cols="10">
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
                            </v-row>
                          </v-card-subtitle>
                        </v-card>
                      </th>
                    </tr>
                  </div>
                </v-col>
              </v-row>
            </v-card>
          </v-scroll-y-transition>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script>
import api from "@/utils/api";

export default {
  name: "PollResultDialog",
  props: {
    currentEvent: {type: Object, required: true},
  },
  data: () => ({
    showDialog: false,
    selectedItem: null,
    selectedUser: null,
    poll: null,
    locations: [],
    timeslots: [],
    disabledTimeslots: [],
    monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    weekdayNames: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
  }),
  methods: {
    getPoll(eventId, userId) {
      api.poll.get(eventId, userId)
          .then(response => this.poll = response)
          .then(() => {
            console.log(this.poll)
            this.locations = this.poll.pollLocations.sort((a, b) => b.points - a.points)
            this.timeslots = this.poll.pollTimeslots.sort((a, b) => b.points - a.points).filter(t => t.points !== 0)
            this.disabledTimeslots = []
            this.poll.pollTimeslots.forEach((item) => {
              if (item.points === 0) {
                this.disabledTimeslots.push(item)
              }
            })
          })
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
  },
  watch: {
    selectedItem: {
      handler(val) {
        this.selectedUser = this.currentEvent.participants[val]
        if (this.selectedUser) {
          this.getPoll(this.currentEvent.id, this.selectedUser.id)
        } else {
          this.poll = null
          this.locations = []
          this.timeslots = []
          this.disabledTimeslots = []
        }
      }
    }
  }
}
</script>

<style scoped>

</style>