<template>
  <v-form
      ref="form"
      v-if="currentEvent"
      v-model="valid"
      lazy-validation>
    <v-text-field
        v-model="currentEvent.name"
        :rules="nameRules"
        prepend-icon="mdi-form-textbox"
        label="Eventname"
        type="text"
    ></v-text-field>
    <v-combobox
        prepend-icon="mdi-account-multiple-plus"
        v-if="availableUsers !== [] && currentEvent.participants"
        v-model="currentEvent.participants"
        :items="availableUsers"
        :search-input.sync="search"
        :filter="filter"
        label="Participants"
        item-text="id"
        multiple
        outlined
        dense
    >
      <template v-slot:no-data>
        <v-container>
          <span class="subheading">User does not exist: </span>
          <v-chip style="margin-left: 10px"
                  :color="'#ff0000'"
                  small
          >
            {{ search }}
          </v-chip>
        </v-container>
      </template>

      <template v-slot:selection="{ attrs, item, parent }">
        <v-chip
            v-bind="attrs"
            color="#437505"
            small
            style="margin: 5px"
        >
          <span class="pr-2">
            {{ item.email }}
          </span>
          <v-icon
              small
              @click="parent.selectItem(item)"
          >
            $delete
          </v-icon>
        </v-chip>
      </template>
      <template v-slot:item="{ index, item }">
        <span :key="item.id">{{ item.firstName }} {{ item.lastName }}</span>
        <v-chip
            small
            style="margin-left: 5px"
            color="#437505">
          {{ item.email }}, {{ item.username }}
        </v-chip>
      </template>
    </v-combobox>
    <template>
      <location-selector/>
    </template>
    <div v-if="loadTimeslots">
      <v-row v-for="(timeslot, index) in event.timeslots" :key="index" ref="timeslots">
        <v-col class=".col-auto">
          <v-datetime-picker label="Select Starttime" v-model="timeslot.start"></v-datetime-picker>
        </v-col>
        <v-col class=".col-auto">
          <v-datetime-picker label="Select Endtime" v-model="timeslot.end"></v-datetime-picker>
        </v-col>
        <v-col v-if="index + 1 === event.timeslots.length" cols="auto" style="margin-top: 10px">
          <v-btn icon @click="addTimeslotInput()">
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </v-col>
        <v-col v-if="index + 1 !== event.timeslots.length" cols="auto" style="margin-top: 10px">
          <v-btn icon @click="removeTimeslotInput(index)">
            <v-icon>mdi-minus</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </div>
  </v-form>
</template>

<script>
import api from "@/utils/api";
import LocationSelector from './LocationSelector.vue';

export default {
  name: "EventForm",
  components: {LocationSelector},
  props: {
    event: {
      type: Object, default: () => ({
        name: '',
        location: null,
        timeslots: [
          {start: null, end: null}
        ],
        participants: [],
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
      console.log(this.event.timeslots);
      console.log(index);
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
  },
  mounted() {
    this.currentEvent = this.event;
    api.user.getAll().then(response => this.availableUsers = response)
  }
}
</script>

<style scoped>

</style>