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
            @validated="edit"/>
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

import api from "@/utils/api";
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
  }),
  methods: {
    tryEdit() {
      this.$refs.form.validate()
    },
    edit(event) {
      api.location.edit(event)
          .then(response => {
            this.success = response.status === 200;
            this.response = response.data.msg
          })

    },
    closeDialog() {
      this.$refs.form.clear()
      this.$emit("close")
      this.response = ''
      this.success = undefined
      this.wrongLocationName = false
    }
  }
}
</script>