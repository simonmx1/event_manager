<template>
  <v-combobox
    prepend-icon="mdi-map-marker-plus"
    v-model="model"
    :items="availableLocations"
    :search-input.sync="search"
    :filter="filter"
    :error-messages="errorMessage"
    label="Locations"
    item-text="id"
    multiple
    outlined
    dense
  >
    <template v-slot:no-data>
      <v-container>
        <span class="subheading">Location does not exist: </span>
        <v-chip style="margin-left: 10px" :color="'#ff0000'" small>
          {{ search }}
        </v-chip>
      </v-container>
    </template>

    <template v-slot:selection="{ attrs, item, parent }">
      <v-chip v-bind="attrs" color="#437505" small>
        <span class="pr-2">
          {{ item.name }}
        </span>
        <v-icon small @click="parent.selectItem(item)"> $delete </v-icon>
      </v-chip>
    </template>
    <template v-slot:item="{ index, item }">
      <span :key="item.id">{{ item.name }}</span>
      <v-spacer />
      
      <location-info-dialog :current-location="item"/>
    </template>
  </v-combobox>
</template>

<script>
import api from "@/utils/api";
import LocationInfoDialog from "@/components/LocationInfoDialog.vue"

export default {
  name: "LocationSelector",
  props: {
    errorMessage: {type: String, default: null}
  },
  components: { LocationInfoDialog },
  data: () => ({
    availableLocations: [],
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
    api.location.getAll().then((response) => (this.availableLocations = response));
  },
};
</script>