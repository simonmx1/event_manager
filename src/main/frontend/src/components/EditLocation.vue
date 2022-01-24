<template>
  <div class="text-center">
    <v-card class="elevation-12">
      <v-toolbar dark color="primary">
        <v-toolbar-title>Edit Location</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <location-form
            v-if="location != null"
            ref="form"
            :admin="admin"
            :location="location"
            :edit="true"
            @validated="edit"
            @confirm="confirmedOpeningTimes"/>
        <v-alert
            v-if="typeof success !== 'undefined'"
            dense
            outlined
            :type="success ? 'success' : 'error'"
        >
          <strong>{{ response }}</strong>
        </v-alert>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="closeDialog()">Close</v-btn>
        <v-btn color="primary" text @click="tryEdit()">Save Location</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>

import LocationForm from "@/components/LocationForm";

export default {
  name: 'EditLocation',
  components: {LocationForm},
  props: {
    location: {type: Object, required: true},
    admin: {type: Boolean, default: true}
  },
  data: () => ({
    response: '',
    wrongLocationName: false,
    success: undefined,
    editedLocation: null
  }),
  methods: {
    tryEdit() {
      this.$refs.form.validate()
    },
    edit(event) {
      this.editedLocation = event
      this.$refs.form.getOpeningTimes()
    },
    confirmedOpeningTimes(event) {
      let l = JSON.parse(JSON.stringify(this.editedLocation))
      l['openingTimes'] = event
      this.$api.location.edit(JSON.parse(JSON.stringify(l)))
          .then(response => {
            this.success = response.status === 201;
            this.response = response.data
          })
    },
    closeDialog() {
      this.$refs.form.clear()
      this.$emit("close")
      this.response = ''
      this.success = undefined
      this.wrongLocationName = false
      this.editedLocation = null
    }
  }
}
</script>