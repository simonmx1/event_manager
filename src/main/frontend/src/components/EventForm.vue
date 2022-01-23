<template>
  <v-form
      ref="form"
      v-if="currentEvent"
      v-model="valid"
      lazy-validation>
    <v-row>
      <v-col cols="6">
        <v-text-field
            v-model="currentEvent.name"
            :error-messages="eventNameError"
            prepend-icon="mdi-form-textbox"
            label="Event name"
            type="text"
        ></v-text-field>
        <template>
          <participants-selector ref="participantsSelector" @confirm="confirmParticipants"
                                 :error-message="participantsError"/>
        </template>
        <template>
          <location-selector ref="locationSelector" @confirm="confirmLocations" :error-message="locationsError"/>
        </template>
        <v-checkbox v-model="currentEvent.creatorIsPreferred" label="Creator decides on poll tie"/>
      </v-col>
      <v-divider vertical/>
      <v-col cols="6">
        <div v-if="loadTimeslots">
          <v-row
              v-for="(timeslot, index) in timeslots"
              :key="index"
              ref="timeslots">
            <v-col class=".col-auto">
              <v-menu
                  ref="dateMenu"
                  v-model="dateMenu[index]"
                  :close-on-content-click="false"
                  :return-value.sync="timeslot.date"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                      v-model="timeslot.date"
                      label="Date"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker
                    v-model="timeslot.date"
                    label="Date"
                    :min="new Date().toISOString()"
                >
                  <v-spacer></v-spacer>
                  <v-btn
                      text
                      color="primary"
                      @click="dateMenu[index] = false; forceRerender()"
                  >
                    Cancel
                  </v-btn>
                  <v-btn
                      text
                      color="primary"
                      @click="saveTimeslotDate(index, timeslot.date)"
                  >
                    OK
                  </v-btn>
                </v-date-picker>
              </v-menu>
            </v-col>
            <v-col class=".col-auto">
              <v-menu
                  ref="startTimeMenu"
                  v-model="startTimeMenu[index]"
                  :close-on-content-click="false"
                  :return-value.sync="timeslot.start"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                      v-model="timeslot.start"
                      label="Start Time"
                      prepend-icon="mdi-clock"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker
                    :format="'24hr'"
                    :allowedMinutes="allowedStepTimeSlot"
                    label="Start Time"
                    v-model="timeslot.start"
                    :max="timeslot.end">
                  <v-spacer></v-spacer>
                  <v-btn
                      text
                      color="primary"
                      @click="startTimeMenu[index] = false; forceRerender()"
                  >
                    Cancel
                  </v-btn>
                  <v-btn
                      text
                      color="primary"
                      @click="saveTimeslotStart(index, timeslot.start)"
                  >
                    OK
                  </v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
            <v-col class=".col-auto">
              <v-menu
                  ref="endTimeMenu"
                  v-model="endTimeMenu[index]"
                  :close-on-content-click="false"
                  :return-value.sync="timeslot.end"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                      v-model="timeslot.end"
                      label="End Time"
                      prepend-icon="mdi-clock"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker
                    :format="'24hr'"
                    :allowedMinutes="allowedStepTimeSlot"
                    label="End Time"
                    v-model="timeslot.end"
                    :min="timeslot.start">
                  <v-spacer></v-spacer>
                  <v-btn
                      text
                      color="primary"
                      @click="endTimeMenu[index] = false; forceRerender()"
                  >
                    Cancel
                  </v-btn>
                  <v-btn
                      text
                      color="primary"
                      @click="saveTimeslotEnd(index, timeslot.end)"
                  >
                    OK
                  </v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
            <v-col v-if="index + 1 === timeslots.length" cols="auto" style="margin-top: 10px">
              <v-btn v-if="timeslots.length > 1" icon @click="removeTimeslotInput(index)">
                <v-icon>mdi-minus</v-icon>
              </v-btn>
            </v-col>
            <v-col v-if="index + 1 !== timeslots.length" cols="auto" style="margin-top: 10px">
              <v-btn icon @click="removeTimeslotInput(index)">
                <v-icon>mdi-minus</v-icon>
              </v-btn>
            </v-col>
          </v-row>
          <v-btn text @click="addTimeslotInput()" style="width: 100%">
            <v-icon>mdi-plus</v-icon> Add Timeslot
          </v-btn>
          <v-alert
              v-if="timeslotError != null"
              dense
              outlined
              type="error"
          >
            <strong>{{ timeslotError }}</strong>
          </v-alert>
        </div>
        <v-divider/>
        <v-row>
          <v-col class=".col-auto">
            <v-menu
                ref="pollDateMenu"
                v-model="pollDateMenu"
                :close-on-content-click="false"
                :return-value.sync="pollEndDate.date"
                transition="scale-transition"
                offset-y
                min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                    v-model="pollEndDate.date"
                    label="Poll End Date"
                    prepend-icon="mdi-calendar"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                    :error="pollEndDateError != null"
                    :error-messages="pollEndDateError"
                ></v-text-field>
              </template>
              <v-date-picker
                  v-if="pollEndDate"
                  v-model="pollEndDate.date"
                  label="Date"
                  :min="new Date().toISOString()"
                  :max="getFirstTimeslot().date"
              >
                <v-spacer></v-spacer>
                <v-btn
                    text
                    color="primary"
                    @click="pollDateMenu = false; forceRerender()"
                >
                  Cancel
                </v-btn>
                <v-btn
                    text
                    color="primary"
                    @click="$refs.pollDateMenu.save(pollEndDate.date)"
                >
                  OK
                </v-btn>
              </v-date-picker>
            </v-menu>
          </v-col>
          <v-col class=".col-auto">
            <v-menu
                ref="pollTimeMenu"
                v-model="pollTimeMenu"
                :close-on-content-click="false"
                :return-value.sync="pollEndDate.time"
                transition="scale-transition"
                offset-y
                min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                    v-model="pollEndDate.time"
                    label="Poll End Time"
                    prepend-icon="mdi-clock"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                    :error="pollEndDateError != null"
                    :error-messages="pollEndDateError"
                ></v-text-field>
              </template>
              <v-time-picker
                  :format="'24hr'"
                  :allowedMinutes="allowedStepTimeSlot"
                  label="Start Time"
                  v-model="pollEndDate.time"
                  :max="pollEndDate.date === getFirstTimeslot().date ? getFirstTimeslot().start : null"
              >
                <v-spacer></v-spacer>
                <v-btn
                    text
                    color="primary"
                    @click="pollTimeMenu = false; forceRerender()"
                >
                  Cancel
                </v-btn>
                <v-btn
                    text
                    color="primary"
                    @click="$refs.pollTimeMenu.save(pollEndDate.time)"
                >
                  OK
                </v-btn>
              </v-time-picker>
            </v-menu>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-form>
</template>

<script>
import LocationSelector from './LocationSelector.vue';
import ParticipantsSelector from './ParticipantsSelector.vue';

export default {
  name: "EventForm",
  components: {LocationSelector, ParticipantsSelector},
  props: {
    event: {
      type: Object, default: () => ({
        name: '',
        locations: null,
        timeslots: [],
        creatorIsPreferred: false,
        pollEndDate: {date: null, time: null},
        enabled: true,
      })
    },
  },
  data: () => ({
    dateMenu: [false],
    startTimeMenu: [false],
    endTimeMenu: [false],
    pollDateMenu: false,
    pollTimeMenu: false,
    valid: false,
    loadTimeslots: true,
    currentEvent: null,
    availableUsers: [],
    search: null,
    datetime: null,
    participantsError: null,
    locationsError: null,
    eventNameError: null,
    pollEndDateError: null,
    timeslotError: null,
    timeslots: [{date: null, start: null, end: null}],
    pollEndDate: {date: null, time: null}
  }),
  methods: {
    saveTimeslotDate(index, date) {
      this.timeslots[index].date = date
      this.dateMenu[index] = false
      this.forceRerender()
    },
    saveTimeslotStart(index, start) {
      this.timeslots[index].start = start
      this.startTimeMenu[index] = false
      this.forceRerender()
    },
    saveTimeslotEnd(index, end) {
      this.timeslots[index].end = end
      this.endTimeMenu[index] = false
      this.forceRerender()
    },
    getFirstTimeslot() {
      let first = this.timeslots[0]
      this.timeslots.forEach(timeslot => {
        if (first.date > timeslot.date) {
          first = timeslot
        } else {
          if (first.date === timeslot.date && first.start > timeslot.start) {
            first = timeslot
          }
        }
      })
      return first
    },
    addOneHour(timestamp) {
      let date = new Date(timestamp)
      date.setHours(date.getHours() - 1)
      return date
    },
    addTimeslotInput() {
      this.dateMenu.push(false)
      this.startTimeMenu.push(false)
      this.endTimeMenu.push(false)
      this.timeslots.push({date: null, start: null, end: null})
      this.forceRerender()
    },
    removeTimeslotInput(index) {
      this.dateMenu.splice(index, 1)
      this.startTimeMenu.splice(index, 1)
      this.endTimeMenu.splice(index, 1)
      this.timeslots.splice(index, 1)
      this.forceRerender()
    },
    forceRerender() {
      // Removing timeslot picker from the DOM
      this.loadTimeslots = false;
      this.$nextTick(() => {
        // Adding the component back in
        this.loadTimeslots = true;
      });
    },
    allowedStepTimeSlot: m => m % 15 === 0,
    allowedStepTimeEnd: m => m % 5 === 0,
    sendData() {
      this.$refs.locationSelector.sendData()
    },
    confirmLocations(locations) {
      this.currentEvent.locations = locations
      this.$refs.participantsSelector.sendData()

    },
    confirmParticipants(participants) {
      this.currentEvent.participants = participants
      this.tryCreate()

    },
    tryCreate() {
      let valid = true;

      if (this.currentEvent.locations.length === 0) {
        this.locationsError = 'Please select at least one location'
        valid = false
      } else {
        this.locationsError = null
      }
      if (this.currentEvent.participants.length === 0) {
        this.participantsError = 'Please select at least one participant'
        valid = false
      } else {
        this.participantsError = null
      }
      if (this.currentEvent.name.length === 0) {
        this.eventNameError = 'Please enter a name for this event'
        valid = false
      } else {
        this.eventNameError = null
      }
      if (this.pollEndDate.date == null || this.pollEndDate.time == null) {
        this.pollEndDateError = 'Please enter a end date and time for the poll of this event'
        valid = false
      } else {
        this.currentEvent.pollEndDate = this.addOneHour(this.pollEndDate.date + "T" + this.pollEndDate.time + ":00.000Z")
        this.pollEndDateError = null
      }
      this.timeslots.forEach(timeslot => {
        if (timeslot.start == null || timeslot.end == null || timeslot.date == null) {
          this.timeslotError = "Some of your timeslots are invalid"
          valid = false
        } else {
          this.timeslotError = null
          this.currentEvent.timeslots.push(this.timeslotConvert(timeslot))
        }
      })
      if (valid)
        this.$emit('confirm', this.currentEvent)
    },
    timeslotConvert(timeslot) {
      return {
        start: this.addOneHour(timeslot.date + 'T' + timeslot.start + ":00.000Z"),
        end: this.addOneHour(timeslot.date + 'T' + timeslot.end + ":00.000Z"),
      }
    },
    clear() {
      this.participantsError = null
      this.locationsError = null
      this.pollEndDateError = null
      this.timeslotError = null
      this.eventNameError = null
      this.$refs.form.reset()
      this.currentEvent = JSON.parse(JSON.stringify(this.event))
      this.timeslots = {date: null, start: null, end: null}
      this.pollEndDate = this.event.pollEndDate
      this.$forceUpdate()
    }
  },
  mounted() {
    this.currentEvent = JSON.parse(JSON.stringify(this.event))
    this.$api.user.getAll().then(response => this.availableUsers = response)
  },
}
</script>

<style scoped>

</style>