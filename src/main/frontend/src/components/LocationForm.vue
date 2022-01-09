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
    <v-checkbox
        v-model="currentLocation.enabled"
        label="Enable Location"
    ></v-checkbox>
  </v-form>
</template>

<script>
export default {
  name: "LocationForm",
  props: {
    location: {
      type: Object, default: () => ({
        name: '',
        menu: '',
        geolocation: '',
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
  }),
  methods: {
    validate() {
      if (this.$refs.form.validate()) {
        this.$emit('validated', this.currentLocation)
      }
    },
    clear() {
      this.$refs.form.resetValidation()
      this.currentLocation = this.location;
    }
  },
  mounted() {
    this.currentLocation = this.location;
    this.editMode = this.edit
  }
}
</script>

<style scoped>

</style>