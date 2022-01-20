<template>
  <v-combobox
      prepend-icon="mdi-account-multiple-plus"
      v-model="model"
      :items="availableParticipants"
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
  </v-combobox>
</template>

<script>
import api from "@/utils/api";

export default {
  name: "ParticipantsSelector",
  data: () => ({
    availableParticipants: [],
    model: [],
    search: null,
    filter: null,
  }),
  methods: {
    sendData() {
      this.$emit("confirm", this.model)
    }
  },
  mounted() {
    api.user.getAll().then((response) => (this.availableParticipants = response));
  },
};
</script>

<style scoped>

</style>