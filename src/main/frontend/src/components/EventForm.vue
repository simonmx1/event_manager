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
        label="Participant"
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
            color="primary"
            small
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
          <span :key="item.username">{{ item.firstName }} {{ item.lastName }}
            <v-chip>{{ item.email }}</v-chip>
          </span>
      </template>
    </v-combobox>
  </v-form>
</template>

<script>
import api from "@/utils/api";

export default {
  name: "EventForm",
  props: {
    event: {
      type: Object, default: () => ({
        name: '',
        location: null,
        timestamp: null,
        participants: [],
        enabled: true,
      })
    },
  },
  data: () => ({
    valid: false,
    currentEvent: null,
    availableUsers: [],
    search: null,
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
  },
  mounted() {
    this.currentEvent = this.event;
    api.getUsers().then(response => this.availableUsers = response)
  }
}
</script>

<style scoped>

</style>