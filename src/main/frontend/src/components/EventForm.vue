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
              <v-datetime-picker :time-picker-props="{format:'24hr', allowedMinutes:allowedStepTimeSlot}"
                                 label="Select Starttime" v-model="timeslot.start"></v-datetime-picker>
            </v-col>
            <v-col class=".col-auto">
              <v-datetime-picker :time-picker-props="{format:'24hr', allowedMinutes:allowedStepTimeSlot}"
                                 label="Select Endtime" v-model="timeslot.end"></v-datetime-picker>
            </v-col>
            <v-col v-if="index + 1 === currentEvent.timeslots.length" cols="auto" style="margin-top: 10px">
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
        </div>
        <v-divider/>
        <v-datetime-picker
            :time-picker-props="{format:'24hr', allowedMinutes:allowedStepTimeEnd}"
            label="Poll end time"
            v-model="currentEvent.pollEndDate"
            :text-field-props="{errorMessages: pollEndDateError}"/>
      </v-col>
    </v-row>
  </v-form>
</template>

<script>
import api from "@/utils/api";
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
    addTimeslotInput() {
      this.event.timeslots.push({start: null, end: null})
    },
    removeTimeslotInput(index) {
      this.event.timeslots.splice(index, 1)
      this.forceRerender()
    },
    forceRerender() {
      // Removing my-component from the DOM
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
        console.log("pollenddate: ", this.currentEvent.pollEndDate);
        this.pollEndDateError = 'Please enter a end date for the poll of this event'
        valid = false
      } else {
        this.pollEndDateError = null
      }
      if (valid)
        this.$emit('confirm', this.currentEvent)
    },
    clear() {
      this.participantsError = null
      this.locationsError = null
      this.$refs.form.reset()
      this.$forceUpdate()
    }
  },
  mounted() {
    this.currentEvent = this.event;
    api.user.getAll().then(response => this.availableUsers = response)
  }
}
</script>

<style scoped>

</style>