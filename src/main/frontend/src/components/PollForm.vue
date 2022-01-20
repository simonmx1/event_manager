<template>
  <v-row>
    <v-col cols="6">
      <v-card-title>
        Sort your Locations:
      </v-card-title>
      <draggable v-model="locations" group="people" @start="drag=true" @end="drag=false">
        <div v-for="element in locations" :key="element.id">{{element.name}}</div>
      </draggable>
    </v-col>
    <v-divider vertical></v-divider>
    <v-col cols="6">
      <v-card-title>
        Sort your Timeslots:
      </v-card-title>
      <draggable v-model="timeslots" group="people" @start="drag=true" @end="drag=false">
        <div v-for="element in timeslots" :key="element.id">{{element.name}}</div>
      </draggable>
    </v-col>
  </v-row>
</template>

<script>
import draggable from 'vuedraggable'
import api from "../utils/api";

export default {
  name: "PollForm",
  components: {
    draggable
  },
  props: {
    event: {type: Object, required: true}
  },
  data: () => ({
    drag: false,
    poll: null,
    locations: [
      {id: 0, name: "Simon", order: 0},
      {id: 1, name: "Simon1", order: 1},
      {id: 2, name: "Simo2", order: 2},
      {id: 3, name: "Simon3", order: 3},
    ],
    timeslots: []
  }),
  methods: {
    getPoll() {
      console.log(this.event);
      api.user.loggedIn()
          .then(response => api.poll.get(this.event.id, response[0])
              .then(response => this.poll = response)
          )
          .then(() => console.log(this.poll))
    }
  },
  mounted() {
    this.getPoll()
  }
}
</script>

<style scoped>

</style>