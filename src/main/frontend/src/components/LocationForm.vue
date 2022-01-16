<template>
  <v-form
      ref="form"
      v-model="valid"
      lazy-validation
      v-if="currentLocation != null">
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
    <main>
      <tag-selector
          :confirm="confirmTags"
          @confirmed="getTags"
          ref="tagSelector"
      />
    </main>
    <v-checkbox
        v-model="currentLocation.enabled"
        label="Enable Location"
    ></v-checkbox>
  </v-form>
</template>

<script>
import TagSelector from "@/components/TagSelector";

export default {
  name: "LocationForm",
  components: {TagSelector},
  props: {
    location: {
      type: Object, default: () => ({
        name: '',
        menu: '',
        geolocation: '',
        tags: null,
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
    getTags(tags){
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