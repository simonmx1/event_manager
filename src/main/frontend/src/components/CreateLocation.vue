<template>
  <div class="text-center">
    <v-card class="elevation-12">
      <v-toolbar dark color="primary">
        <v-toolbar-title> Create Location </v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <location-form ref="form" :admin="admin" @validated="creation" @confirm="confirmedOpeningTimes"/>
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
        <v-btn color="blue darken-1" text @click="closeDialog()"> Cancel </v-btn>
        <v-btn color="primary" text @click="tryCreation()"> Create Location </v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import LocationForm from "@/components/LocationForm";

export default {
  name: 'CreateLocation',
  components: {LocationForm},
  props: {
    admin: {type: Boolean, default: false}
  },
  data: () => ({
    response: '',
    wrongLocationName: false,
    success: undefined,
    location: null
  }),
  methods: {
    tryCreation() {
      this.$refs.form.validate()
    },
    creation(event) {
      this.location = event
      this.$refs.form.getOpeningTimes()

    },
    confirmedOpeningTimes(event) {
      let l = JSON.parse(JSON.stringify(this.location))
      l['openingTimes'] = event
      this.$api.location.create(JSON.parse(JSON.stringify(l)))
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
    }
  }
}
</script>