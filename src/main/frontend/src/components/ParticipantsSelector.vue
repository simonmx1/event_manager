<template>
  <v-autocomplete
      prepend-icon="mdi-account-multiple-plus"
      v-model="model"
      :items="availableParticipants"
      :search-input.sync="search"
      :filter="filter"
      :hint="'Creator is always a participant'"
      :error-messages="errorMessage"
      persistent-hint
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
          style="margin: 5px"
      >
        <v-chip
            small
            color="#437505">
          {{ item.email }}
        </v-chip>
        <v-chip
            small
            color="#054375">
          {{ item.username }}
        </v-chip>
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
          style="margin-left: 5px"
          small
          color="#437505">
        {{ item.email }}
      </v-chip>
      <v-chip
          small
          style="margin-left: 5px"
          color="#054375">
        {{ item.username }}
      </v-chip>
    </template>
  </v-autocomplete>
</template>

<script>
import api from "@/utils/api";

export default {
  name: "ParticipantsSelector",
  props: {
    errorMessage: {type: String, default: null}
  },
  data: () => ({
    availableParticipants: [],
    model: [],
    search: null,
  }),
  methods: {
    sendData() {
      this.$emit("confirm", this.model)
    },
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
  },
  mounted() {
    api.user.getAll().then((response) => (this.availableParticipants = response))
        .then(() => api.user.loggedIn()
            .then(response => this.availableParticipants.splice(
                this.availableParticipants.findIndex(user => user.username === response[0]), 1))
        )
  },
};
</script>

<style scoped>

</style>