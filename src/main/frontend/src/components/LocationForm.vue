<template>
  <v-form
      ref="form"
      v-model="valid"
      lazy-validation
      v-if="currentLocation != null">
    <v-row>
      <v-col cols="6">
        <v-text-field
            v-model="currentLocation.name"
            :rules="nameRules"
            prepend-icon="mdi-account-box"
            name="name"
            label="Name"
            type="text"
        ></v-text-field>
        <v-text-field
            v-model="currentLocation.menu"
            :rules="menuRules"
            prepend-icon="mdi-lock"
            name="menu"
            label="Menu Link"
            type="text"
        ></v-text-field>
        <v-divider></v-divider>
        <v-text-field
            v-model="currentLocation.geolocation"
            :rules="geolocationRules"
            prepend-icon="mdi-form-textbox"
            name="geolocation"
            label="Geo Location"
            type="text"
        ></v-text-field>
        <tag-selector
            :confirm="confirmTags"
            :tags="currentLocation.tags"
            prepend-icon="mdi-tag-multiple"
            @confirmed="getTags"
            ref="tagSelector"
            style="padding-left: 20px; margin-left: 12px"
        />
        <v-textarea
            v-model="currentLocation.description"
            :rules="descriptionRules"
            prepend-icon="mdi-card-text"
            name="description"
            label="Description"
            type="text"
            counter="255"
            outlined></v-textarea>
        <v-checkbox
            v-model="currentLocation.enabled"
            label="Enable Location"
        ></v-checkbox>
      </v-col>
      <v-divider
          vertical
      ></v-divider>
      <v-col cols="6">
        <opening-times-selector
            prepend-icon="mdi-clock"
        />
      </v-col>
    </v-row>

  </v-form>
</template>

<script>
import TagSelector from "@/components/TagSelector";
import OpeningTimesSelector from "@/components/OpeningTimesSelector";

export default {
  name: "LocationForm",
  components: {OpeningTimesSelector, TagSelector},
  props: {
    location: {
      type: Object, default: () => ({
        name: '',
        menu: '',
        geolocation: '',
        description: '',
        tags: [],
        enabled: true,
      })
    },
  },
  data: () => ({
    currentLocation: null,
    valid: true,
    nameRules: [
      v => !!v || 'Name is required'
    ],
    menuRules: [
      v => !!v || 'Link to Menu is required',
    ],
    geolocationRules: [
      v => !!v || 'Position is required',
    ],
    descriptionRules: [
      v => v.length <= 255 || 'Description max length is 255',
    ],
    confirmTags: false
  }),
  methods: {
    validate() {
      if (this.$refs.form.validate()) {
        this.confirmTags = true
      }
    },
    clear() {
      this.$refs.form.resetValidation()
      this.$refs.tagSelector.clear()
      this.currentLocation = JSON.parse(JSON.stringify(this.location));
    },
    getTags(tags) {
      this.currentLocation.tags = tags;
      this.$emit('validated', this.currentLocation)
    }
  },
  mounted() {
    this.editMode = this.edit
    this.currentLocation = JSON.parse(JSON.stringify(this.location));
  },
}
</script>

<style scoped>

</style>