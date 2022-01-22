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
            :rules="nameRules"
            prepend-icon="mdi-form-textbox"
            label="Eventname"
            type="text"
        ></v-text-field>
        <template>
          <participants-selector ref="participantsSelector" @confirm="confirmParticipants"/>
        </template>
        <template>
          <location-selector ref="locationSelector" @confirm="confirmLocations"/>
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
        <v-datetime-picker :time-picker-props="{format:'24hr', allowedMinutes:allowedStepTimeEnd}"
                           label="Poll end time" v-model="currentEvent.pollEndDate"></v-datetime-picker>
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
        location: null,
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
    nameRules: [
      v => !!v || 'Eventname is required'
    ]
  }),
  methods: {
    filter(item, queryText, itemText) {
      const hasValue = val => val != null ? val : ''

      const query = hasValue(queryText)
      const text = hasValue(itemText)

      return item.username.toString().toLowerCase().indexOf(query.toString().toLowerCase()) > -1
          || item.email.toString().toLowerCase().indexOf(query.toString().toLowerCase()) > -1
          || item.firstName.toString().toLowerCase().indexOf(query.toString().toLowerCase()) > -1
          || item.lastName.toString().toLowerCase().indexOf(query.toString().toLowerCase()) > -1
          || text.toString().toLowerCase().indexOf(query.toString().toLowerCase()) > -1;
    },
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
      this.currentEvent.location = locations
      this.$refs.participantsSelector.sendData()
    },
    confirmParticipants(participants) {
      this.currentEvent.participants = participants
      this.$emit('confirm', this.currentEvent)
    },
    clear() {
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