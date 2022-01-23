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
          <v-row v-for="(timeslot, index) in currentEvent.timeslots" :key="index" ref="timeslots">
            <v-col class=".col-auto">
              <v-datetime-picker
                  :time-picker-props="{format:'24hr', allowedMinutes:allowedStepTimeSlot, max: timeslot.end != null ? timeslot.end.getTime().toString() : null}"
                  :date-picker-props="{min: new Date().toISOString(), max: timeslot.end != null ? timeslot.end.toISOString() : null}"
                  label="Select Starttime"
                  v-model="timeslot.start">
                <template v-slot:dateIcon>
                  <v-icon>mdi-calendar</v-icon>
                </template>
                <template v-slot:timeIcon>
                  <v-icon>mdi-clock</v-icon>
                </template>
              </v-datetime-picker>
            </v-col>
            <v-col class=".col-auto">
              <v-datetime-picker :time-picker-props="{format:'24hr', allowedMinutes:allowedStepTimeSlot}"
                                 :date-picker-props="{min: getMinEndDate(timeslot) != null ? getMinEndDate(timeslot) : new Date().toISOString()}"
                                 label="Select Endtime" v-model="timeslot.end">
                <template v-slot:dateIcon>
                  <v-icon>mdi-calendar</v-icon>
                </template>
                <template v-slot:timeIcon>
                  <v-icon>mdi-clock</v-icon>
                </template>
              </v-datetime-picker>
            </v-col>
            <v-col v-if="index + 1 === currentEvent.timeslots.length" cols="auto" style="margin-top: 10px">
              <v-btn v-if="currentEvent.timeslots.length > 1" icon @click="removeTimeslotInput(index)">
                <v-icon>mdi-minus</v-icon>
              </v-btn>
              <v-btn icon @click="addTimeslotInput()">
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </v-col>
            <v-col v-if="index + 1 !== currentEvent.timeslots.length" cols="auto" style="margin-top: 10px">
              <v-btn icon @click="removeTimeslotInput(index)">
                <v-icon>mdi-minus</v-icon>
              </v-btn>
            </v-col>
          </v-row>
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
        <v-datetime-picker
            :time-picker-props="{format:'24hr', allowedMinutes:allowedStepTimeEnd}"
            :date-picker-props="{min: new Date().toISOString(), max: pollEndDateMax.date}"
            label="Poll end time"
            v-model="currentEvent.pollEndDate"
            :text-field-props="{errorMessages: pollEndDateError}">
          <template v-slot:dateIcon>
            <v-icon>mdi-calendar</v-icon>
          </template>
          <template v-slot:timeIcon>
            <v-icon>mdi-clock</v-icon>
          </template>
        </v-datetime-picker>
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
        timeslots: [
          {start: null, end: null}
        ],
        creatorIsPreferred: false,
        pollEndDate: null,
        enabled: true,
      })
    },
  },
  data: () => ({
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
  }),
  methods: {
    getFirstTimeslotStart() {
      let first = this.currentEvent.timeslots[0]
      this.currentEvent.timeslots.forEach(timeslot => {
        if (first.start <= timeslot.start) {
          first = timeslot
        }
      })
      return {
        date: first.start != null ? this.addOneHour(first.start).toISOString() : null,
        time: first.start != null ? first.start.getTime() : null
      }
    },
    getMinEndDate(timeslot) {
      return timeslot.start != null ? this.addOneHour(timeslot.start).toISOString() : null
    },
    addOneHour(timestamp) {
      let date = new Date(timestamp)
      date.setHours(date.getHours() + 1)
      return date
    },
    addTimeslotInput() {
      this.currentEvent.timeslots.push({start: null, end: null})
    },
    removeTimeslotInput(index) {
      this.currentEvent.timeslots.splice(index, 1)
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
      if (this.currentEvent.pollEndDate == null) {
        this.pollEndDateError = 'Please enter a end date for the poll of this event'
        valid = false
      } else {
        this.pollEndDateError = null
      }
      this.currentEvent.timeslots.forEach(timeslot => {
        if (timeslot.start == null || timeslot.end == null) {
          this.timeslotError = "Some of your timeslots are invalid"
          valid = false
        } else {
          this.timeslotError = null
        }
      })
      if (valid)
        this.$emit('confirm', this.currentEvent)
    },
    clear() {
      this.participantsError = null
      this.locationsError = null
      this.pollEndDateError = null
      this.timeslotError = null
      this.eventNameError = null
      this.$refs.form.reset()
      this.currentEvent = JSON.parse(JSON.stringify(this.event))
      this.$forceUpdate()
    }
  },
  mounted() {
    this.currentEvent = JSON.parse(JSON.stringify(this.event))
    this.$api.user.getAll().then(response => this.availableUsers = response)
  },
  computed: {
    pollEndDateMax: function () {
      return this.getFirstTimeslotStart()
    }
  },
  watch: {
    '$data.currentEvent.timeslots': {
      handler: function (val) {
        console.log(val)
      },
      deep: true
    }
  }
}
</script>

<style scoped>

</style>