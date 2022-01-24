<template>
  <v-autocomplete
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
    auto-select-first
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
  </v-autocomplete>
</template>

<script>
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
  }),
  methods: {
    sendData() {
      let locations = []
      this.model.forEach(id => locations.push(this.availableLocations.find(location => location.id === id)))
      this.$emit("confirm", locations)
    },
    filter(item, queryText, itemText) {
      const hasValue = val => val != null ? val : ''
      const query = hasValue(queryText)
      const text = hasValue(itemText)

      return item.name.toString().toLowerCase().indexOf(query.toString().toLowerCase()) > -1
          || text.toString().toLowerCase().indexOf(query.toString().toLowerCase()) > -1;
    },
    clear() {
      this.model = [];
    }
  },
  mounted() {
    this.$api.location.getAll().then((response) => (this.availableLocations = response));
  }
};
</script>